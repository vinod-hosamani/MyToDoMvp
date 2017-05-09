package com.bridgelabz.mytodomvp.homescreen.ui.activity;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */
public interface HomeScreenActivityInterface extends NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,TodoItemAdapter.OnNoteClickListener, SearchView.OnQueryTextListener
{
    void getNoteSuccess(List<TodoItemModel> noteList);
    void getNoteFailure(String message);
    void showProgressDialogue(String message);
    void hideProgressDialogu();

    void deleteTodoModelFailure();
    void deleteTodoModelSuccess();
    void moveToArchiveFailure();
    void moveToArchiveSuccess();


}
