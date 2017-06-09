package com.bridgelabz.mytodomvp.homescreen.ui.fragment;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 27/5/17.
 */
public interface TrashFragmentInterface
{
    void showProgressDialog(String message);
    void hideProgressDialog();

    void getNoteListSuccess(List<TodoItemModel> noteList);
    void getNoteListFailure(String message);

     void permanentDeleteSuccess( String message);
     void permanentDaleteFailure(String message);

    void moveToArchiveFromTrashSuccess(String message);
    void moveToArchiveFromTrashFailure(String message);
}
