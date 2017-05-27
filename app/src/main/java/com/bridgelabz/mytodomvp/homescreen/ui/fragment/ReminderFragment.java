package com.bridgelabz.mytodomvp.homescreen.ui.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.ReminderPresenter;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public class ReminderFragment extends Fragment implements ReminderFragmentInterface
{

    public static final String TAG = "ReminderFragment";

    Context mContext;
    RecyclerView archiveRecyclerView;
    TodoItemAdapter todoItemAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    HomeScreenActivity homeScreenActivity;
    ReminderPresenter presenter;


    @Override
    public void onResume()
    {
        super.onResume();
        ((HomeScreenActivity)getActivity()).setTitle("Reminder");
    }

    public ReminderFragment(Context context)
    {
        this.mContext=context;
        presenter=new ReminderPresenter(mContext,this);

    }

    private void initView(View view)
    {
        archiveRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_archvied_list);
        setHasOptionsMenu(true);
        todoItemAdapter=new TodoItemAdapter(homeScreenActivity);
        archiveRecyclerView.setAdapter(todoItemAdapter);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        archiveRecyclerView.setLayoutManager(staggeredGridLayoutManager);
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
        presenter.getTodayReminderList(userId);
        return view;
    }

    ProgressDialog progressDialog;
    @Override
    public void showProgressDialog(String message)
    {
            progressDialog=new ProgressDialog(mContext);
            progressDialog.setMessage(message);
            progressDialog.show();

    }

    @Override
    public void hideProgrssDialog()
    {
        progressDialog.dismiss();
    }

    @Override
    public void getTodayReminderSuccess(List<TodoItemModel> noteList)
    {
        List<TodoItemModel> reminderList=new ArrayList<>();
        SimpleDateFormat format= new SimpleDateFormat("dd MMM yyyy");

        String currentDate=format.format(new Date().getTime());

        for(TodoItemModel model: noteList)
        {
            if(!model.getReminderDate().equals(currentDate)&& !model.isArchieved())
            {
                reminderList.add(model);
            }
        }
        todoItemAdapter.setTodoList(reminderList);
    }

    @Override
    public void getTodayReinderFailure(String message)
    {
        Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
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
        int id = item.getItemId();
        if (id == R.id.action_toggle)
        {
            toggle(item);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isList=true;
    private void toggle(MenuItem item)
    {
        MenuItem items = item;
        if (isList)
        {
            staggeredGridLayoutManager.setSpanCount(2);
            items.setIcon(R.drawable.ic_action_list);
            items.setTitle("Show as list");
            isList = false;
        }
        else
        {
            staggeredGridLayoutManager.setSpanCount(1);
            items.setIcon(R.drawable.ic_action_grid);
            items.setTitle("Show as grid");
            isList = true;
        }
    }





}
