package com.bridgelabz.mytodomvp.homescreen.ui.fragment;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.TodoNotesPresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.TodoNotesPresenterInterface;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.bridgelabz.mytodomvp.session.SessionManagement;
import com.bridgelabz.mytodomvp.util.SwipeAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public class TodoNotesFragment extends Fragment implements TodoNotesFragmentInterface
{

    public static final String TAG = "TodoNotesFragment";

    HomeScreenActivity homeScreenActivity;
    TodoNotesPresenterInterface presenter;
    SessionManagement session;
    List<TodoItemModel> allData;
    TodoItemAdapter todoItemAdapter;
    boolean isList;
    RecyclerView toDoItemRecycler;
    StaggeredGridLayoutManager mstaggeredGridLayoutManager;
    SwipeAction swipeAction;
    ItemTouchHelper itemTouchHelper;
    AddToDoFragment todoNoteaddFragment;
    TodoItemAdapter recyclerAdapter;
    AddToDoFragment fragment;

    @Override
    public void onResume()
    {
        super.onResume();
        ((HomeScreenActivity)getActivity()).setTitle("Notes");
    }

    public TodoNotesFragment(HomeScreenActivity homeScreenActivity)
    {
        this.homeScreenActivity=homeScreenActivity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.content_homescreen,container,false);
        initView(view);
        setHasOptionsMenu(true);
        presenter.getTodoNoteFromServer(session.getUserDetails().getId());
        allData=todoItemAdapter.getAllDataList();
        return view;
    }

    private void initView(View view)
     {
         isList=true;
         toDoItemRecycler=(RecyclerView)view.findViewById(R.id.recycler_todo_Item);
         todoItemAdapter=new TodoItemAdapter(homeScreenActivity,this);

         session=new SessionManagement(homeScreenActivity);
         presenter=new TodoNotesPresenter(homeScreenActivity,this);

         mstaggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
         toDoItemRecycler.setLayoutManager(mstaggeredGridLayoutManager);
         toDoItemRecycler.setAdapter(todoItemAdapter);

         swipeAction=new SwipeAction(SwipeAction.up|SwipeAction.down|SwipeAction.left |SwipeAction.right,SwipeAction.left |SwipeAction.right,todoItemAdapter,homeScreenActivity);
         itemTouchHelper=new ItemTouchHelper(swipeAction);
         itemTouchHelper.attachToRecyclerView(toDoItemRecycler);
     }
    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList)
    {
      List<TodoItemModel> nonArchivedList=new ArrayList<>();
        for(TodoItemModel model:noteList)
        {
            if(!model.isArchieved())
            {
                if(!model.isDeleted())
                {
                    nonArchivedList.add(model);
                }
            }
        }
        todoItemAdapter.setTodoList(nonArchivedList);
    }

    @Override
    public void getNoteFailure( String message)
    {
        Toast.makeText(homeScreenActivity,message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;
    @Override
    public void progressDialog(String message)
    {
        if(!homeScreenActivity.isFinishing())
        {
            progressDialog=new ProgressDialog(homeScreenActivity);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }
    @Override
    public void hideProgressDialog()
    {
      if(!homeScreenActivity.isFinishing() && progressDialog!=null)
      {
          progressDialog.dismiss();
      }
    }

   /* @Override
    public void moveToArchiveFailure(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void moveToArchiveSuccess(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void moveToTrashFailure(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void moveToTrashSuccess(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

    }*/
   /* @Override
    public void deleteTodoModelFailure(String message)
    {
        Toast.makeText(homeScreenActivity,message, Toast.LENGTH_SHORT).show();
    }*/

  /*  @Override
    public void delteTodoModelSuccess(String messsage)
    {
        Toast.makeText(homeScreenActivity,messsage,Toast.LENGTH_SHORT).show();
    }
*/
   /* @Override
    public void moveToArchvieFailure(String message)
    {
        Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }
*/
  /*  @Override
    public void moveToArchvieSuccess(String message)
    {
        Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onClick(View view)
    {

    }
    @Override
    public void onItemClick(int pos)
    {
        homeScreenActivity.addTodoFab.setVisibility(View.INVISIBLE);
        TodoItemModel model=todoItemAdapter.getItemModel(pos);

        Bundle argument=new Bundle();
        argument.putString(Constant.key_title,model.getTitle());
        argument.putString(Constant.key_note,model.getNote());
        argument.putString(Constant.key_reminder,model.getReminderDate());
        argument.putInt(Constant.key_note_id, model.getNoteId());
        argument.putString(Constant.key_startDate,model.getStartDate());


        fragment=new AddToDoFragment(homeScreenActivity);
        AddToDoFragment.add=false;
        AddToDoFragment.editposition=pos;

        fragment.setArguments(argument);

        homeScreenActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer,fragment,"editTodo")
                .addToBackStack(null)
                .commit();
        todoItemAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String searchText)
    {
        searchText=searchText.toLowerCase();
        List<TodoItemModel> noteList=new ArrayList<>();
        for(TodoItemModel model: allData)
        {
            if(model.getTitle().toLowerCase().contains(searchText))
            {
                noteList.add(model);
            }
        }
        todoItemAdapter.setFilter(noteList);
        return  true;
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_toggle)
        {
            toggle(item);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

*/


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar,menu);
        /*menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar,menu);*/
        // this.menu=menu;

        SearchManager searchManager=(SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_toggle)
        {
            Log.d("menu select","onOptionsItemSelected: toggle");
            toggle(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void toggle(MenuItem items)
    {
        if (isList)
        {
            mstaggeredGridLayoutManager.setSpanCount(2);
            items.setIcon(R.drawable.list_view);
            items.setTitle("Show as list");
            isList = false;
        }
        else
        {
            mstaggeredGridLayoutManager.setSpanCount(1);
            items.setIcon(R.drawable.grid_view);
            items.setTitle("Show as grid");
            isList = true;
        }
    }
    public void setColor(int color)
    {
        fragment.setColor(color);
    }
}
