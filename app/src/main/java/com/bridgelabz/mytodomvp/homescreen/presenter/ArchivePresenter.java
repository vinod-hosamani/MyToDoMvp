package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.homescreen.interactor.ArchiveInteractor;
import com.bridgelabz.mytodomvp.homescreen.interactor.ArchiveInteractorInterfacce;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.ArchiveFragmentInterface;

import java.util.List;

/**
 * Created by bridgeit on 11/5/17.
 */
public class ArchivePresenter implements ArchivePresenterInterface{

    Context mContext;
    ArchiveFragmentInterface viewInterface;
    ArchiveInteractorInterfacce interactor;

    public ArchivePresenter(Context context, ArchiveFragmentInterface viewInterface){
        this.mContext = context;
        this.viewInterface=viewInterface;
        interactor=new ArchiveInteractor(mContext,this);
    }
    @Override
    public void getNoteList(String userId)
    {
      interactor.getNoteList(userId);
    }

    @Override
    public void getNoteListSuccess(List<TodoItemModel> noteList)
    {
     viewInterface.getNoteListSuccess(noteList);
    }

    @Override
    public void getNoteListFailure(String message)
    {
     viewInterface.getNoteListFailure(message);
    }

    @Override
    public void showProgressDialogue(String message)
    {
     viewInterface.showProgressDialougeu(message);
    }

    @Override
    public void hidePregressDialogu()
    {
        viewInterface.hideProgressDialogue();

    }

    @Override
    public void moveFailure(String message) {
        viewInterface.goToNotesFailure(message);
    }

    @Override
    public void moveSuccess(String message)
    {
       viewInterface.goToNotesSuccess(message);
    }

    @Override
    public void goToTrash(TodoItemModel itemModel)
    {
       interactor.goToTrash(itemModel);
    }

    @Override
    public void goToNotes(TodoItemModel itemModel)
    {
        interactor.goToNotes(itemModel);
    }



    @Override
    public void goToTrshSuccess(String message)
    {
        viewInterface.goToTrashSuccess(message);
    }

    @Override
    public void goToTrashFailure(String message)
    {
          viewInterface.goToTrashFailure(message);
    }

    @Override
    public void goToNotesSuccess(String message)
    {
        viewInterface.goToNotesSuccess(message);
    }

    @Override
    public void goToNotesFailure(String message)
    {
       viewInterface.goToNotesFailure(message);
    }

    @Override
    public void notesAgainFromTrash(TodoItemModel itemModel)
    {
       interactor.notesAgainFromTrash(itemModel);
    }

    @Override
    public void notesAgainFromNotes(TodoItemModel itemModel)
    {
        interactor.notesAgainFromNotes(itemModel);
    }

    @Override
    public void notesAgainFromTrashSuccess(String message) {
         viewInterface.notesAgainFromTrashSuccess(message);
    }

    @Override
    public void notesAgainFromTrashFailure(String message) {
       viewInterface.notesAgainFromTrashFailure(message);
    }

    @Override
    public void notesAgainFromNotesSuccess(String message) {
 viewInterface.notesAgainFromNotesSuccess(message);
    }

    @Override
    public void notesAgainFromNotesFailure(String message) {
  viewInterface.notesAgainFromNotesFailure(message);
    }
}
