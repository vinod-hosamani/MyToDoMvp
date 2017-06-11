package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.homescreen.interactor.TodoNotesInteractor;
import com.bridgelabz.mytodomvp.homescreen.interactor.TodoNotesInteractorInteraface;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.TodoNotesFragmentInterface;

import java.util.List;

/**
 * Created by bridgeit on 15/5/17.
 */
public class TodoNotesPresenter implements TodoNotesPresenterInterface
{

    TodoNotesFragmentInterface viewInterface;
    TodoNotesInteractorInteraface interactor;

    public TodoNotesPresenter(Context context,TodoNotesFragmentInterface viewInterface)
    {
        this.viewInterface=viewInterface;
        interactor=new TodoNotesInteractor(context,this);
    }
    @Override
    public void getTodoNoteFromServer(String userId)
    {
     interactor.getTodoNoteFromServer(userId);
    }

    @Override
    public void getTodoNoteSuccess(List<TodoItemModel> noteList)
    {
       viewInterface.getNoteSuccess(noteList);
    }

    @Override
    public void getNoteFailure(String message)
    {
       viewInterface.getNoteFailure(message);
    }

    @Override
    public void showProgressDialog(String message)
    {
       viewInterface.progressDialog(message);
    }

    @Override
    public void hideProgressDilogu() {
        viewInterface.hideProgressDialog();
    }

    @Override
    public void moveToArchive(TodoItemModel itemModel) {
        interactor.motoToArchive(itemModel);
    }

    @Override
    public void moveToArchiveFailure(String message) {
        viewInterface.moveToArchiveFailure(message);
    }

    @Override
    public void moveToArchiveSuccess(String message) {
      viewInterface.moveToArchiveSuccess(message);
    }

    @Override
    public void moveToTrash(TodoItemModel itemModel) {
        interactor.moveToTrash(itemModel);
    }

    @Override
    public void moveToTrashFailure(String message) {
      viewInterface.moveToTrashFailure(message);
    }

    @Override
    public void moveToTrashSuccess(String message) {
      viewInterface.moveToTrashSuccess(message);
    }

    @Override
    public void moveToNotes(TodoItemModel itemModel) {
        interactor.moveToNotes(itemModel);
    }

    @Override
    public void moveToNotesSuccess(String message) {
     viewInterface.moveToNotesSuccess(message);
    }

    @Override
    public void moveToNotesFailure(String message) {
      viewInterface.moveToNotesFailure(message);
    }

    @Override
    public void moveToNotesFromTrash(TodoItemModel itemModel) {
interactor.moveToNotesFromTrash(itemModel);
    }

    @Override
    public void moveToNotesFromTrashSuccess(String message) {
   viewInterface.moveToNotesFromTrashSuccess(message);
    }

    @Override
    public void moveToNotesFromTrashFailure(String message) {
    viewInterface.moveToNotesFromTrashFailure(message);
    }
  /*  @Override
    public void deletoTodoModelFailure(String message)
    {
    viewInterface.deleteTodoModelFailure(message);
    }

    @Override
    public void deleteTodoModelSuccess(String message)
    {
    viewInterface.delteTodoModelSuccess(message);
    }*/

   /* @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos)
    {
     interactor.deleteTodoModel(tempList,itemModel,pos);
    }

    @Override
    public void moveToArchive(TodoItemModel itemModel)
    {
     interactor.movetToArchive(itemModel);
    }
*/
/*
    @Override
    public void moveFailure(String message)
    {
      viewInterface.moveToArchvieFailure(message);
    }

    @Override
    public void moveSuccess(String message)
    {
       viewInterface.moveToArchvieSuccess(message);
    }
*/

   /* @Override
    public void moveToNotes(TodoItemModel itemModel)
    {
        interactor.moveToNotes(itemModel);
    }*/
}
