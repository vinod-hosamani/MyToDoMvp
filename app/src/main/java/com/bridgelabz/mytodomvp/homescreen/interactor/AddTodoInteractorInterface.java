package com.bridgelabz.mytodomvp.homescreen.interactor;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 10/5/17.
 */
public interface AddTodoInteractorInterface
{
    void getResponseForAddTodoToServer(TodoItemModel model,String userId);
    void getResponseForUpdateTodoToServer(TodoItemModel model,String userId);
}
