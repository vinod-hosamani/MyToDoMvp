package com.bridgelabz.mytodomvp.homescreen.presenter;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 27/5/17.
 */
public interface TrashPresenterInterface {

    void showProgressDialog(String message);
    void hideProgressDialog();
    void getNoteList(String userId);
    void getNoteListSuccess(List<TodoItemModel> noteList);
    void getNoteListFailure(String failure);

    void permanentDelete(List<TodoItemModel> tempList,TodoItemModel itemModel,int pos);

    void permanentDeleteSuccess(String message);
    void permanentDeleteFailure(String message);


    void moveToArchiveFromTrash(TodoItemModel itemModel);
    void moveToArchiveFromTrashSuccess(String message);
    void moveToArchvieFromTrashFailure(String message);

}
