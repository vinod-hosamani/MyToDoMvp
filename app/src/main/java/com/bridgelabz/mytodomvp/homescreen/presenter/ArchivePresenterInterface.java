package com.bridgelabz.mytodomvp.homescreen.presenter;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 11/5/17.
 */
public interface ArchivePresenterInterface
{
    void getNoteList(String userId);
    void getNoteListSuccess(List<TodoItemModel> noteList);
    void getNoteListFailure(String message);
    void showProgressDialogue(String message);
    void hidePregressDialogu();
}
