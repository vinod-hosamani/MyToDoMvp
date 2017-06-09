package com.bridgelabz.mytodomvp.homescreen.interactor;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 27/5/17.
 */
public interface TrashInteractorInterface
{
    void getNoteList(String userId);
    void permanetDelete(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos);
    void moveToArchiveFromTrash(TodoItemModel itemModel);
}
