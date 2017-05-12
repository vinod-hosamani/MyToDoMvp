package com.bridgelabz.mytodomvp.homescreen.presenter;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 10/5/17.
 */
public interface AddTodoPresenterInterface
{


    void addTodoSuccess(String message);
    void addTodoFailure(String message);
    void showProgressDialogue(String message);
    void hideProgressDialogue();
    void updateSuccess(String message);
    void updateFailure(String message);

    void getResponseForUpdateTodoToServer(TodoItemModel model,String userId);
    void getResponseForAddTodoToServer(TodoItemModel model,String userId);


}
