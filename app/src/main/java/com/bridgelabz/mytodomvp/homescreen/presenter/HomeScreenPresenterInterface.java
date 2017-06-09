package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.net.Uri;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */
public interface HomeScreenPresenterInterface
{
    void getTodoNoteFromServer(String userId);
    void getNoteSuccess(List<TodoItemModel> noteList);
    void getNoteFailure(String message);
    void showProgressDailogue(String message);
    void hideProgressDailogue();

    //void deleteTodoModelFailure(String message);
    //void deleteTodoModelSuccess(String message);

    //void deleteTodoModel(List<TodoItemModel> tempList,TodoItemModel itemModel,int pos);

    void moveToArchive(TodoItemModel itemModel);
    void moveToFailure(String message);
    void moveToSuccess(String message);
    void moveToNotes(TodoItemModel itemModel);
    void moveToNotesFromTrash(TodoItemModel itemModel);

    void moveToTrash(TodoItemModel itemModel);
    void moveToTrashFailure(String message);
    void moveToTrashSuccess(String message);
    //void MoveToTrashNotes(TodoItemModel itemModel);

    //void moveToReminder(TodoItemModel itemModel);
    void moveToReminderFailure(String message);
    void moveToReminderSuccess(String message);


    void uploadProfilePic(String currentUserId, Uri selectedImage);
    void uploadSuccess(Uri selectedImage);
    void uploadFailure(String message);
}
