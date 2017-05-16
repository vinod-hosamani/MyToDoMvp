package com.app.todo.homescreen.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.todo.R;
import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.ArchievedPresenter;
import com.app.todo.homescreen.presenter.ArchievedPresenterInterface;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 3/5/17.
 */

public class ArchievedFragment extends Fragment implements ArchievedFragmentInterface {

    RecyclerView archievedRecyclerView;
    TodoItemAdapter todoItemAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    HomeScreenActivity homeScreenActivity;

    ArchievedPresenterInterface presenter;

    public ArchievedFragment(HomeScreenActivity homeScreenActivity){
        this.homeScreenActivity = homeScreenActivity;
        presenter = new ArchievedPresenter(homeScreenActivity, this);
    }

    private void initView(View view) {
        archievedRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_archieved_list);

        homeScreenActivity.setTitle(getString(R.string.archieve));
        setHasOptionsMenu(true);

        todoItemAdapter = new TodoItemAdapter(homeScreenActivity, this);
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

        presenter.getNoteList(userId);

        return view;
    }

    @Override
    public void getNoteListSuccess(List<TodoItemModel> noteList) {
        List<TodoItemModel> archievedList = new ArrayList<>();

        for (TodoItemModel model :
                noteList) {
            if (model.isArchieved())
                archievedList.add(model);
        }
        todoItemAdapter.setTodoList(archievedList);
    }

    @Override
    public void getNoteListFailure(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;

    @Override
    public void showProgressDialog(String message) {

        progressDialog = new ProgressDialog(homeScreenActivity);
        if(!homeScreenActivity.isFinishing()){
            progressDialog.setMessage(message);
            progressDialog.show();
        }

    }

    @Override
    public void hideProgressDialog() {
        if (!homeScreenActivity.isFinishing() && progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onLongClick(final TodoItemModel itemModel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(homeScreenActivity);
        builder.setTitle(homeScreenActivity.getString(R.string.moving_to_note));
        builder.setMessage(homeScreenActivity.getString(R.string.ask_move_to_note_message));
        builder.setPositiveButton(homeScreenActivity.getString(R.string.ok_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        homeScreenActivity.presenter.moveToNotes(itemModel);
                    }
                });

        builder.setNegativeButton(homeScreenActivity.getString(R.string.cancel_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(homeScreenActivity
                                , homeScreenActivity.getString(R.string.cancel_message)
                                , Toast.LENGTH_SHORT).show();
                    }
                });

        builder.show();
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
        if(item.getItemId() == R.id.action_toggle) {
            Log.d("menu select", "onOptionsItemSelected: toggle");
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