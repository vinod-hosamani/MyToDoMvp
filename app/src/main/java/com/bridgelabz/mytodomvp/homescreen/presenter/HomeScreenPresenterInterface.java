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


    void uploadProfilePic(String currentUserId, Uri selectedImage);
    void uploadSuccess(Uri selectedImage);
    void uploadFailure(String message);
}
