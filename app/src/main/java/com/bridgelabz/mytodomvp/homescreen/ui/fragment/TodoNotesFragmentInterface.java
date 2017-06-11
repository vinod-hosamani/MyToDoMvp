package com.bridgelabz.mytodomvp.homescreen.ui.fragment;

import android.support.v7.widget.SearchView;
import android.view.View;

import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public interface TodoNotesFragmentInterface extends View.OnClickListener,
        TodoItemAdapter.OnNoteClickListener,SearchView.OnQueryTextListener
{
    void getNoteSuccess(List<TodoItemModel> noteList);
    void getNoteFailure(String message);

    void progressDialog(String message);
    void hideProgressDialog();



    void moveToArchiveFailure(String message);
    void moveToArchiveSuccess(String message);

    void moveToTrashFailure(String message);
    void moveToTrashSuccess(String message);


    void moveToNotesSuccess(String message);
    void moveToNotesFailure(String message);

    void moveToNotesFromTrashSuccess(String message);
    void moveToNotesFromTrashFailure(String message);




}
