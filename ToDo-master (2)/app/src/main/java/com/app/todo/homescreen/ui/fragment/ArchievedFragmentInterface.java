package com.app.todo.homescreen.ui.fragment;

import android.view.View;

import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 3/5/17.
 */

public interface ArchievedFragmentInterface extends TodoItemAdapter.OnLongClickListener{

    void getNoteListSuccess(List<TodoItemModel> noteList);
    void getNoteListFailure(String message);
    void showProgressDialog(String message);
    void hideProgressDialog();

}