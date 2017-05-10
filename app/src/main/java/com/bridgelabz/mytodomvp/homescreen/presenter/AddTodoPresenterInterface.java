package com.bridgelabz.mytodomvp.homescreen.presenter;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 10/5/17.
 */
public interface AddTodoPresenterInterface
{
    void getResponseForAddTodoToServer(TodoItemModel model,String userId);
    void addTodoSuccess(String message);
    void addTodoFailure(String message);
    void showProgressDialogue(String message);
    void hideProgressDialogue();
    void getResponseForUpdateTodoToServer(TodoItemModel model,String userId);
    void updateSuccess(String message);
    void updateFailure(String message);

}
