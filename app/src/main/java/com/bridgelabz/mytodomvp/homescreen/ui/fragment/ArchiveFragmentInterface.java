package com.bridgelabz.mytodomvp.homescreen.ui.fragment;

import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 11/5/17.
 */
public interface ArchiveFragmentInterface extends TodoItemAdapter.OnLongClickListener
{
     void getNoteListSuccess(List<TodoItemModel> noteList);
     void getNoteListFailure(String message);

     void showProgressDialougeu(String message);
     void hideProgressDialogue();

     void goToTrashSuccess(String message);
     void goToTrashFailure(String message);

     void goToNotesSuccess(String message);
     void goToNotesFailure(String message);

     void notesAgainFromTrashSuccess(String message);
     void notesAgainFromTrashFailure(String message);

     void notesAgainFromNotesSuccess(String message);
     void notesAgainFromNotesFailure(String message);
}
