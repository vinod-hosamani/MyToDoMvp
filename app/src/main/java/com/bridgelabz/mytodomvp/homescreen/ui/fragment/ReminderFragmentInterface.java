package com.bridgelabz.mytodomvp.homescreen.ui.fragment;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public interface ReminderFragmentInterface {
    void showProgressDialog(String message);
    void hideProgrssDialog();

    void getTodayReminderSuccess(List<TodoItemModel> noteList);
    void getTodayReinderFailure(String message);

    void moveToTrashSuccess(String message);
    void moveToTrashFailure(String message);

    void moveToReminderFailure(String message);
    void moveToReminderSuccess(String message);
}
