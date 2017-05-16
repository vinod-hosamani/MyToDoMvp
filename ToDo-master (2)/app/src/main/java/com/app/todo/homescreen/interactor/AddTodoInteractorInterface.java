package com.app.todo.homescreen.interactor;

import com.app.todo.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 22/4/17.
 */

public interface AddTodoInteractorInterface {
    void getResponseForAddTodoToServer(TodoItemModel model, String userId);

    void getResponseForUpdateTodoToServer(TodoItemModel model, String userId);
}