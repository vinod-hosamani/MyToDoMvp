package com.bridgelabz.mytodomvp.homescreen.interactor;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

/**
 * Created by bridgeit on 11/5/17.
 */
public interface ArchiveInteractorInterfacce
{
    void getNoteList(String userId);

    void goToTrash(TodoItemModel itemModel);
    void goToNotes(TodoItemModel itemModel);

    void notesAgainFromTrash(TodoItemModel itemModel);
    void notesAgainFromNotes(TodoItemModel itemModel);
}
