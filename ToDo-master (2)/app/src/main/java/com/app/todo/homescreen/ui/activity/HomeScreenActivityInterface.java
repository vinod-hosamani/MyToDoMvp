package com.app.todo.homescreen.ui.activity;

import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 24/4/17.
 */

public interface HomeScreenActivityInterface extends NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, TodoItemAdapter.OnNoteClickListener, SearchView.OnQueryTextListener{
    void getNoteSuccess(List<TodoItemModel> noteList);
    void getNoteFailure(String message);
    void showDialog(String message);
    void hideDialog();

    void deleteTodoModelFailure(String message);

    void deleteTodoModelSuccess(String message);

    void moveToArchieveFailure(String message);

    void moveToArchieveSuccess(String message);

    void uploadSuccess(Uri downloadUrl);

    void uploadFailure(String message);
}