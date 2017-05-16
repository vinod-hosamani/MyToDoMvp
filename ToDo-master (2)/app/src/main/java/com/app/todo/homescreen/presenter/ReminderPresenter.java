package com.app.todo.homescreen.presenter;

import android.content.Context;

import com.app.todo.homescreen.interactor.ReminderInteractor;
import com.app.todo.homescreen.interactor.ReminderInteractorInterface;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.ui.fragment.ReminderFragmentInterface;

import java.util.List;

/**
 * Created by bridgeit on 8/5/17.
 */

public class ReminderPresenter implements ReminderPresenterInterface {

    Context context;
    ReminderFragmentInterface viewInterface;

    ReminderInteractorInterface interactor;

    public ReminderPresenter(Context context, ReminderFragmentInterface viewInterface) {
        this.context = context;
        this.viewInterface = viewInterface;

        interactor = new ReminderInteractor(context, this);
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
    public void getTodayReminderSuccess(List<TodoItemModel> noteList) {
        viewInterface.getTodayReminderSuccess(noteList);
    }

    @Override
    public void getTodayReminderFailure(String message) {
        viewInterface.getTodayReminderFailure(message);
    }

    @Override
    public void getTodayReminderList(String userId) {
        interactor.getTodayReminderList(userId);
    }
}