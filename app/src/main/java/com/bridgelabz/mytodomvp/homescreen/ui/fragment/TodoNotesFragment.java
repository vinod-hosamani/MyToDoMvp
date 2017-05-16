package com.bridgelabz.mytodomvp.homescreen.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
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
public class TodoNotesFragment extends Fragment implements TodoNotesFragmentInterface {
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



    public TodoNotesFragment(HomeScreenActivity homeScreenActivity)
    {
        this.homeScreenActivity=homeScreenActivity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_homescreen,container,false);
        initView(view);
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
    swipeAction=new SwipeAction(0,SwipeAction.left | SwipeAction.right,todoItemAdapter,homeScreenActivity);
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
                nonArchivedList.add(model);
            }
        }
        todoItemAdapter.setTodoList(nonArchivedList);
    }

    @Override
    public void getNoteFailure( String message) {
        Toast.makeText(homeScreenActivity,message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;
    @Override
    public void progressDialog(String message) {
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

    @Override
    public void deleteTodoModelFailure(String message) {
        Toast.makeText(homeScreenActivity,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delteTodoModelSuccess(String messsage) {
  Toast.makeText(homeScreenActivity,messsage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchvieFailure(String message) {
    Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchvieSuccess(String message) {
   Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

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
       // argument.putString(Constant.key_note_id,model.getNoteId());
        argument.putString(Constant.key_note_id, String.valueOf(model.getNoteId()));
        argument.putString(Constant.key_startDate,model.getStartDate());

        AddToDoFragment fragment=new AddToDoFragment(homeScreenActivity);
        AddToDoFragment.add=false;
        AddToDoFragment.editposition=pos;
        fragment.setArguments(argument);
        homeScreenActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.todo_item_fragment,fragment,"editTodo")
                .addToBackStack(null)
                .commit();
        todoItemAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
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
}
