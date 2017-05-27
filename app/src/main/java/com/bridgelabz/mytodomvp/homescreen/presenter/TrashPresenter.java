package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.homescreen.interactor.TrashInteractor;
import com.bridgelabz.mytodomvp.homescreen.interactor.TrashInteractorInterface;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.TrashFragmentInterface;

import java.util.List;

/**
 * Created by bridgeit on 27/5/17.
 */
public class TrashPresenter implements TrashPresenterInterface
{

    Context mContext;
    TrashFragmentInterface viewInterface;
    TrashInteractorInterface interactor;

    public TrashPresenter(Context context, TrashFragmentInterface viewInterface )
    {
        this.mContext=context;
        this.viewInterface=viewInterface;
        interactor=new TrashInteractor(mContext,this);

    }


    @Override
    public void showProgressDialog(String message)
    {
      viewInterface.showProgressDialog(message);
    }

    @Override
    public void hideProgressDialog()
    {
     viewInterface.hideProgressDialog();
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
    public void getNoteListFailure(String failure)
    {
      viewInterface.getNoteListFailure(failure);
    }
}
