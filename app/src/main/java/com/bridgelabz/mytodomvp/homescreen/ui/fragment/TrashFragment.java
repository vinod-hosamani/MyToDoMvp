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
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.TrashPresenter;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.bridgelabz.mytodomvp.util.SwipeTrash;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 27/5/17.
 */
public class TrashFragment extends Fragment implements TrashFragmentInterface, SearchView.OnQueryTextListener {
    public static final String TAG ="TrashFragment";
    Context mContext;
    RecyclerView trashRecyclerview;
    TodoItemAdapter totoItemAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    //HomeScreenActivity homeScreenActivity;
   // TrashPresenter presenter;
    //Menu menu;
    ProgressDialog progressDialog;
    List<TodoItemModel> trashItemModels=new ArrayList<>();
    ItemTouchHelper itemTouchHelper;
    private SwipeTrash swipeTrash;
    List<TodoItemModel> allData;
    public  TrashPresenter presenter;


    @Override
    public void onResume()
    {
        super.onResume();
        ((HomeScreenActivity)getActivity()).setTitle("Trash");
    }


    public TrashFragment(Context context)
    {
        this.mContext=context;
        presenter=new TrashPresenter(mContext,this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_archived_list,container,false);
        initView(view);
        setHasOptionsMenu(true);
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        presenter.getNoteList(userId);
        return view;
    }

    public void initView(View view)
    {
        trashRecyclerview=(RecyclerView)view.findViewById(R.id.recycler_archvied_list);
        setHasOptionsMenu(true);
        totoItemAdapter=new TodoItemAdapter(mContext,this);
        trashRecyclerview.setAdapter(totoItemAdapter);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,staggeredGridLayoutManager.VERTICAL);
        trashRecyclerview.setLayoutManager(staggeredGridLayoutManager);

        swipeTrash=new SwipeTrash(SwipeTrash.up|SwipeTrash.down|SwipeTrash.left|SwipeTrash.right,SwipeTrash.left | SwipeTrash.right,totoItemAdapter,TrashFragment.this,getActivity());
        itemTouchHelper=new ItemTouchHelper(swipeTrash);
        itemTouchHelper.attachToRecyclerView(trashRecyclerview);

    }
    @Override
    public void showProgressDialog(String message)
    {
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    @Override
    public void hideProgressDialog()
    {
        progressDialog.dismiss();
    }

    @Override
    public void getNoteListSuccess(List<TodoItemModel> noteList)
    {
        List<TodoItemModel> trashList=new ArrayList<>();
        for(TodoItemModel model:noteList)
        {
            if(model.isDeleted())
/*
                if(!model.isArchieved())
*/
                trashList.add(model);
        }
        allData=trashList;
        trashItemModels=trashList;
        totoItemAdapter.setTodoList(trashList);
    }



    @Override
    public void getNoteListFailure(String message)
    {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permanentDeleteSuccess(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permanentDaleteFailure(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchiveFromTrashSuccess(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchiveFromTrashFailure(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

   /* @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.search).setVisible(false);
        this.menu=menu;
        super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.trashmenu,menu);

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

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.archivemenu,menu);
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
    */
    private boolean isList=true;

    private void toggle(MenuItem item)
    {
        if(isList)
        {
            staggeredGridLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.list_view);
            item.setTitle("show as list");
            isList=false;
        }
        else
        {
            staggeredGridLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.grid_view);
            item.setTitle("isGrid");
            isList=true;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        searchText=searchText.toLowerCase();
        List<TodoItemModel> noteList=new ArrayList<>();
        for(TodoItemModel model: allData)
        {
            if (model.getTitle().toLowerCase().contains(searchText)) {
                noteList.add(model);
            }

        }
        totoItemAdapter.setFilter(noteList);
        return  true;
    }
}
