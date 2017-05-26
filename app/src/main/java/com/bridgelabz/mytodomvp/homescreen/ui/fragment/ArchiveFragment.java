package com.bridgelabz.mytodomvp.homescreen.ui.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.ArchivePresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.ArchivePresenterInterface;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 11/5/17.
 */
public class ArchiveFragment extends Fragment implements ArchiveFragmentInterface
{
    RecyclerView archiveRecyclerView;
    TodoItemAdapter totoItemAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    HomeScreenActivity homeScreenActivity;
    ArchivePresenterInterface presenter;

    public ArchiveFragment(HomeScreenActivity homeScreenActivity)
    {
        this.homeScreenActivity=homeScreenActivity;
        presenter=new ArchivePresenter(homeScreenActivity,this);
    }

    public void initView(View view)
    {
        archiveRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_archvied_list);
        homeScreenActivity.setTitle("archive");
        setHasOptionsMenu(true);
        totoItemAdapter=new TodoItemAdapter(homeScreenActivity,this);
        archiveRecyclerView.setAdapter(totoItemAdapter);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,staggeredGridLayoutManager.VERTICAL);
        archiveRecyclerView.setLayoutManager(staggeredGridLayoutManager);

    }
    @Override
    public void getNoteListSuccess(List<TodoItemModel> noteList)
    {
        List<TodoItemModel> archiveList=new ArrayList<>();
        for(TodoItemModel model:noteList)
        {
            if(model.isArchieved())
                archiveList.add(model);
        }
        totoItemAdapter.setTodoList(archiveList);

    }

    @Override
    public void getNoteListFailure(String message)
    {
        Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;
    @Override
    public void showProgressDialougeu(String message)
    {
     progressDialog=new ProgressDialog(homeScreenActivity);
        if(!homeScreenActivity.isFinishing())
        {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialogue()
    {
     if(!homeScreenActivity.isFinishing() && progressDialog!=null)
     {
      progressDialog.dismiss();
     }
    }

    @Override
    public void onLongClick( final  TodoItemModel itemModel)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(homeScreenActivity);
        builder.setTitle(homeScreenActivity.getString(R.string.moingToNotes));
        builder.setMessage(homeScreenActivity.getString(R.string.askMoveToNoteMessage));
        builder.setPositiveButton(homeScreenActivity.getString(R.string.okButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
               homeScreenActivity.presenter.moveToNotes(itemModel);
            }
        });

        builder.setNegativeButton(homeScreenActivity.getString(R.string.cancleButton),
                new DialogInterface.OnClickListener()
                {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                   Toast.makeText(homeScreenActivity,homeScreenActivity.getString(R.string.cancel_message),
                   Toast.LENGTH_SHORT).show();
            }
        });

     builder.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_archived_list,container,false);
        initView( view);
        setHasOptionsMenu(true);
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        presenter.getNoteList(userId);
        return view;
    }

   Menu menu;

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.search).setVisible(false);
        this.menu=menu;
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /*if(item.getItemId()==R.id.action_toggle);
        {    toggle();
            return true;
        }

      //  return super.onOptionsItemSelected(item);*/
        if(item.getItemId() == R.id.action_toggle)
        {
            Log.d("menu select","onOptionsItemSelected: toggle");
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isList=true;

    private void toggle()
    {
        MenuItem item=menu.findItem(R.id.action_toggle);
        if(isList)
        {
            staggeredGridLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("show as list");
            isList=false;
        }
        else
        {
            staggeredGridLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("isGrid");
            isList=true;
        }

    }
}
