package com.app.todo.homescreen.presenter;

import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 8/5/17.
 */

public interface ReminderPresenterInterface {
    void showDialog(String message);
    void hideDialog();

    void getTodayReminderSuccess(List<TodoItemModel> noteList);
    void getTodayReminderFailure(String message);

    void getTodayReminderList(String userId);
}