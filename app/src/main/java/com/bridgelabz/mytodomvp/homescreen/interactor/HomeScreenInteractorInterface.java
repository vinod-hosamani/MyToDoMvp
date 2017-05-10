package com.bridgelabz.mytodomvp.homescreen.interactor;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */
public interface HomeScreenInteractorInterface
{

    void getTodoNoteFromServer(String userId);
    void deleteTodoModel(List<TodoItemModel> tempList,TodoItemModel itemModel,int pos);
    void motoToArchive(TodoItemModel itemModel);
    void moveToNotes(TodoItemModel itemModel);
}