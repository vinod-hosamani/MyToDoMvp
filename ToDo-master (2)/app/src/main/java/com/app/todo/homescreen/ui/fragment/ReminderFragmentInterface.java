package com.app.todo.homescreen.ui.fragment;

import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 8/5/17.
 */

public interface ReminderFragmentInterface {
    void showDialog(String message);
    void hideDialog();

    void getTodayReminderSuccess(List<TodoItemModel> noteList);
    void getTodayReminderFailure(String message);
}