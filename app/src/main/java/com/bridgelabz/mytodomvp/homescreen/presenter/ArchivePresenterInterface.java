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

    void moveFailure(String message);
    void moveSuccess(String message);

    void goToTrash(TodoItemModel itemModel);
    void goToNotes(TodoItemModel itemModel);

    void goToTrshSuccess(String message);
    void goToTrashFailure(String message);

    void goToNotesSuccess(String message);
    void goToNotesFailure(String message);

    void notesAgainFromTrash(TodoItemModel itemModel);
    void notesAgainFromNotes(TodoItemModel itemModel);


    void notesAgainFromTrashSuccess(String message);
    void notesAgainFromTrashFailure(String message);

    void notesAgainFromNotesSuccess(String message);
    void notesAgainFromNotesFailure(String message);

}
