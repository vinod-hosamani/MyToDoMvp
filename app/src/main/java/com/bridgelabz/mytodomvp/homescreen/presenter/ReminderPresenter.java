package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.homescreen.interactor.ReminderInteractor;
import com.bridgelabz.mytodomvp.homescreen.interactor.ReminderInteractorInterface;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.ReminderFragmentInterface;

import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public class ReminderPresenter implements ReminderPresenterIterface {
    Context context;
    ReminderFragmentInterface viewInterface;
    ReminderInteractorInterface interactor;


    public ReminderPresenter(Context context,ReminderFragmentInterface viewInterface)
    {
        this.context = context;
        this.viewInterface = viewInterface;

        interactor =  new ReminderInteractor(context, this);
    }
    @Override
    public void showProgressDilogu(String message) {
         viewInterface.showProgressDialog(message);
    }

    @Override
    public void hideProgressDialgu() {
viewInterface.hideProgrssDialog();
    }

    @Override
    public void getTodayReminderSuccess(List<TodoItemModel> noteList) {
     viewInterface.getTodayReminderSuccess(noteList);
    }

    @Override
    public void getTodayReminderFailure(String message) {
     viewInterface.getTodayReinderFailure(message);
    }

    @Override
    public void getTodayReminderList(String userId) {
    interactor.getTodayReminderList(userId);
    }
}
