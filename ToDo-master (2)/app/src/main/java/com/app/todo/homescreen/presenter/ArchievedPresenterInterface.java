package com.app.todo.homescreen.presenter;

import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 3/5/17.
 */

public interface ArchievedPresenterInterface {
    void getNoteList(String userId);

    void getNoteListSuccess(List<TodoItemModel> noteList);
    void getNoteListFailure(String message);
    void showProgressDialog(String message);
    void hideProgressDialog();
}
