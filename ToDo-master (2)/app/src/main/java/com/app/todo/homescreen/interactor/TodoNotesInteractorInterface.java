package com.app.todo.homescreen.interactor;

import com.app.todo.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 12/5/17.
 */

public interface TodoNotesInteractorInterface {
    void getTodoNoteFromServer(String userId);

    void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos);

    void moveToArchieve(TodoItemModel itemModel);

    void moveToNotes(TodoItemModel itemModel);
}