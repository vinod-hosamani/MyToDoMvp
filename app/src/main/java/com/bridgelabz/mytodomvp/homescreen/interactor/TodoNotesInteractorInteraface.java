package com.bridgelabz.mytodomvp.homescreen.interactor;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 15/5/17.
 */
public interface TodoNotesInteractorInteraface
{

    void getTodoNoteFromServer(String userId);
    //void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos);


   // void movetToArchive(TodoItemModel itemModel);
    //void moveToNotes(TodoItemModel itemModel);

    void motoToArchive(TodoItemModel itemModel);
    void moveToTrash(TodoItemModel itemModel);

    void moveToNotes(TodoItemModel itemModel);
    void moveToNotesFromTrash(TodoItemModel itemModel);

}
