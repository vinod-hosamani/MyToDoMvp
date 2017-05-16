package com.app.todo.homescreen.ui.fragment;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 12/5/17.
 */

public interface TodoNotesFragmentInterface extends View.OnClickListener,
        TodoItemAdapter.OnNoteClickListener, SearchView.OnQueryTextListener {

    void getNoteSuccess(List<TodoItemModel> noteList);
    void getNoteFailure(String message);
    void showDialog(String message);
    void hideDialog();

    void deleteTodoModelFailure(String message);

    void deleteTodoModelSuccess(String message);

    void moveToArchieveFailure(String message);

    void moveToArchieveSuccess(String message);

}