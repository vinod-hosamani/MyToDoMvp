package com.bridgelabz.mytodomvp.homescreen.presenter;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public interface ReminderPresenterIterface
{
    void showProgressDilogu(String message);
    void hideProgressDialgu();

    void getTodayReminderSuccess(List<TodoItemModel> noteList);
    void getTodayReminderFailure(String message);

    void getTodayReminderList(String userId);



    void moveToTrash(TodoItemModel itemModel);
    void moveToTrashSuccess(String message);
    void moveToTrashFailure(String message);
}
