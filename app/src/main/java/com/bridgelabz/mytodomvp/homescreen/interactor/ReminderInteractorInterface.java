package com.bridgelabz.mytodomvp.homescreen.interactor;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 15/5/17.
 */
public interface ReminderInteractorInterface
{
    void getTodayReminderList(String userId);
    void moveToTrash(TodoItemModel itemModel);
}
