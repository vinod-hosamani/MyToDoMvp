package com.bridgelabz.mytodomvp.homescreen.presenter;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public interface TodoNotesPresenterInterface
{
    void getTodoNoteFromServer(String userId);
    void getTodoNoteSuccess(List<TodoItemModel> noteList);
    void getNoteFailure(String message);
    void showProgressDialog(String message);
    void hideProgressDilogu();

    void deletoTodoModelFailure(String message);
    void deleteTodoModelSuccess(String message);
    void deleteTodoModel(List<TodoItemModel> tempList,TodoItemModel itemModel ,int pos);
    void moveToArchive(TodoItemModel itemModel);
    void moveFailure(String message);
    void moveSuccess(String message);
    void moveToNotes(TodoItemModel itemModel);


}
