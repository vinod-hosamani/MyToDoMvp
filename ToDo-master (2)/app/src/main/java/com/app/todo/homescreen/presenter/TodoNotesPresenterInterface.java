package com.app.todo.homescreen.presenter;

import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 12/5/17.
 */

public interface TodoNotesPresenterInterface {
    void getTodoNoteFromServer(String userId);
    void getNoteSuccess(List<TodoItemModel> noteList);
    void getNoteFailure(String message);
    void showDialog(String message);
    void hideDialog();

    void deleteTodoModelFailure(String message);

    void deleteTodoModelSuccess(String message);

    void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos);

    void moveToArchieve(TodoItemModel itemModel);

    void moveFailure(String message);

    void moveSuccess(String message);

    void moveToNotes(TodoItemModel itemModel);
}