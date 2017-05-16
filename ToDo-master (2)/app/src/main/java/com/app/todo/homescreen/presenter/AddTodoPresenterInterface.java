package com.app.todo.homescreen.presenter;

import com.app.todo.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 22/4/17.
 */

public interface AddTodoPresenterInterface {

    void getResponseForAddTodoToServer(TodoItemModel model, String userId);

    void addTodoSuccess(String message);
    void addTodoFailure(String message);
    void showDialog(String message);
    void hideDialog();

    void getResponseForUpdateTodoToServer(TodoItemModel model, String userId);

    void updateSuccess(String message);

    void updateFailure(String message);
}