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
}
