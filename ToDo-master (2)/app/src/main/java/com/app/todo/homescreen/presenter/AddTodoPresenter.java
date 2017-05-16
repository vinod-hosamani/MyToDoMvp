package com.app.todo.homescreen.presenter;

import android.content.Context;

import com.app.todo.homescreen.interactor.AddTodoInteractor;
import com.app.todo.homescreen.interactor.AddTodoInteractorInterface;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.ui.fragment.AddTodoFragment;
import com.app.todo.homescreen.ui.fragment.AddTodoFragmentInterface;

/**
 * Created by bridgeit on 22/4/17.
 */

public class AddTodoPresenter implements AddTodoPresenterInterface {

    Context context;
    AddTodoFragmentInterface viewInterface;
    AddTodoInteractorInterface interactor;

    public AddTodoPresenter(Context context, AddTodoFragment viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        interactor = new AddTodoInteractor(context, this);
    }

    @Override
    public void getResponseForAddTodoToServer(TodoItemModel model, String userId) {
        interactor.getResponseForAddTodoToServer(model, userId);
    }

    @Override
    public void getResponseForUpdateTodoToServer(TodoItemModel model, String userId) {
        interactor.getResponseForUpdateTodoToServer(model, userId);
    }

    @Override
    public void addTodoSuccess(String message) {
        viewInterface.addTodoSuccess(message);
    }

    @Override
    public void addTodoFailure(String message) {
        viewInterface.addTodoFailure(message);
    }

    @Override
    public void updateSuccess(String message) {
        viewInterface.updateSuccess(message);
    }

    @Override
    public void updateFailure(String message) {
        viewInterface.updateFailure(message);
    }

    @Override
    public void showDialog(String message) {
        viewInterface.showDialog(message);
    }

    @Override
    public void hideDialog() {
        viewInterface.hideDialog();
    }
}