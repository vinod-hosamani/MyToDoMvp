package com.app.todo.homescreen.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.todo.R;
import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.ReminderPresenter;
import com.app.todo.homescreen.presenter.ReminderPresenterInterface;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.id.toggle;

public class ReminderFragment extends Fragment implements ReminderFragmentInterface {
    RecyclerView archievedRecyclerView;
    TodoItemAdapter todoItemAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    HomeScreenActivity homeScreenActivity;

    ReminderPresenterInterface presenter;

    public ReminderFragment(HomeScreenActivity homeScreenActivity){
        this.homeScreenActivity = homeScreenActivity;
        presenter = new ReminderPresenter(homeScreenActivity, this);
    }

    private void initView(View view) {
        archievedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_archieved_list);
        setHasOptionsMenu(true);

        todoItemAdapter = new TodoItemAdapter(homeScreenActivity);
        archievedRecyclerView.setAdapter(todoItemAdapter);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager
                (1, StaggeredGridLayoutManager.VERTICAL);
        archievedRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_archieved_list, container, false);
        initView(view);

        setHasOptionsMenu(true);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        presenter.getTodayReminderList(userId);

        return view;
    }

    ProgressDialog progressDialog;

    @Override
    public void showDialog(String message) {
        progressDialog = new ProgressDialog(homeScreenActivity);
        if(!homeScreenActivity.isFinishing()){
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if(!homeScreenActivity.isFinishing() && progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void getTodayReminderSuccess(List<TodoItemModel> noteList) {
        List<TodoItemModel> reminderList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat(homeScreenActivity
                .getString(R.string.date_formate));
        String currentDate = format.format(new Date().getTime());

        for (TodoItemModel model :
                noteList) {
            if(model.getReminderDate().equals(currentDate) && !model.isArchieved()){
                reminderList.add(model);
            }
        }
        todoItemAdapter.setTodoList(reminderList);
    }

    @Override
    public void getTodayReminderFailure(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    Menu menu;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
        this.menu = menu;
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_toggle:
                toggle();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isList = true;

    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);

        if (isList) {
            staggeredGridLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");
            isList = false;
        } else {
            staggeredGridLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            isList = true;
        }
    }
}