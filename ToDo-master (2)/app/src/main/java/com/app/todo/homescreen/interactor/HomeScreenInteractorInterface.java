package com.app.todo.homescreen.interactor;

import android.net.Uri;

import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 24/4/17.
 */

public interface HomeScreenInteractorInterface {
    void getTodoNoteFromServer(String userId);

    void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos);

    void moveToArchieve(TodoItemModel itemModel);

    void moveToNotes(TodoItemModel itemModel);

    void uploadProfilePic(String currentUserId, Uri selectedImage);
}