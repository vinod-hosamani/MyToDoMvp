package com.app.todo.homescreen.presenter;

import android.content.Context;

import com.app.todo.homescreen.interactor.HomeScreenInteractor;
import com.app.todo.homescreen.interactor.TodoNotesInteractor;
import com.app.todo.homescreen.interactor.TodoNotesInteractorInterface;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.app.todo.homescreen.ui.fragment.TodoNotesFragment;
import com.app.todo.homescreen.ui.fragment.TodoNotesFragmentInterface;

import java.util.List;

/**
 * Created by bridgeit on 12/5/17.
 */

public class TodoNotesPresenter implements TodoNotesPresenterInterface {

    TodoNotesFragmentInterface viewInterface;
    TodoNotesInteractorInterface interactor;

    public TodoNotesPresenter(Context context, TodoNotesFragmentInterface viewInterface) {
        this.viewInterface = viewInterface;
        interactor = new TodoNotesInteractor(context, this);
    }

    @Override
    public void getTodoNoteFromServer(String userId) {
        interactor.getTodoNoteFromServer(userId);
    }

    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList) {
        viewInterface.getNoteSuccess(noteList);
    }

    @Override
    public void getNoteFailure(String message) {
        viewInterface.getNoteFailure(message);
    }

    @Override
    public void showDialog(String message) {
        viewInterface.showDialog(message);
    }

    @Override
    public void hideDialog() {
        viewInterface.hideDialog();
    }

    @Override
    public void deleteTodoModelFailure(String message) {
        viewInterface.deleteTodoModelFailure(message);
    }

    @Override
    public void deleteTodoModelSuccess(String message) {
        viewInterface.deleteTodoModelSuccess(message);
    }

    @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos) {
        interactor.deleteTodoModel(tempList, itemModel, pos);
    }

    @Override
    public void moveToArchieve(TodoItemModel itemModel) {
        interactor.moveToArchieve(itemModel);
    }

    @Override
    public void moveFailure(String message) {
        viewInterface.moveToArchieveFailure(message);
    }

    @Override
    public void moveSuccess(String message) {
        viewInterface.moveToArchieveSuccess(message);
    }

    @Override
    public void moveToNotes(TodoItemModel itemModel) {
        interactor.moveToNotes(itemModel);
    }
}