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
}
