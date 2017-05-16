package com.app.todo.homescreen.presenter;

import android.content.Context;

import com.app.todo.homescreen.interactor.ArchievedInteractor;
import com.app.todo.homescreen.interactor.ArchievedInteractorInterface;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.app.todo.homescreen.ui.fragment.ArchievedFragment;
import com.app.todo.homescreen.ui.fragment.ArchievedFragmentInterface;

import java.util.List;

/**
 * Created by bridgeit on 3/5/17.
 */

public class ArchievedPresenter implements ArchievedPresenterInterface {

    Context context;
    ArchievedFragmentInterface viewInterface;

    ArchievedInteractorInterface interactor;

    public ArchievedPresenter(Context context, ArchievedFragment viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        interactor = new ArchievedInteractor(context, this);
    }

    @Override
    public void getNoteList(String userId) {
        interactor.getNoteList(userId);
    }

    @Override
    public void getNoteListSuccess(List<TodoItemModel> noteList) {
        viewInterface.getNoteListSuccess(noteList);
    }

    @Override
    public void getNoteListFailure(String message) {
        viewInterface.getNoteListFailure(message);
    }

    @Override
    public void showProgressDialog(String message) {
        viewInterface.showProgressDialog(message);
    }

    @Override
    public void hideProgressDialog() {
        viewInterface.hideProgressDialog();
    }
}
