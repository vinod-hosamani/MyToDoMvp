package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.homescreen.interactor.AddTodoInteractor;
import com.bridgelabz.mytodomvp.homescreen.interactor.AddTodoInteractorInterface;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.AddToDoFragment;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.AddToDoFragmentInterface;

/**
 * Created by bridgeit on 10/5/17.
 */
public class AddTodoPresenter implements AddTodoPresenterInterface {

    Context context;
    AddToDoFragmentInterface viewInterface;
    AddTodoInteractorInterface interactor;


    public AddTodoPresenter(Context context, AddToDoFragment viewInterface)
    {
        this.context=context;
        this.viewInterface=viewInterface;
        interactor=new AddTodoInteractor(context,this);
    }

    @Override
    public void getResponseForAddTodoToServer(TodoItemModel model, String userId)
    {
        interactor.getResponseForAddTodoToServer(model,userId);

    }

    @Override
    public void addTodoSuccess(String message)
    {
        viewInterface.addTodoSuccess(message);

    }

    @Override
    public void addTodoFailure(String message)
    {
       viewInterface.addTodoFailure(message);
    }

    @Override
    public void showProgressDialogue(String message)
    {
      viewInterface.showProgressDailogue(message);
    }

    @Override
    public void hideProgressDialogue()
    {
        viewInterface.hideProgressDialogue();

    }

    @Override
    public void getResponseForUpdateTodoToServer(TodoItemModel model, String userId)
    {
       interactor.getResponseForUpdateTodoToServer(model, userId);
    }

    @Override
    public void updateSuccess(String message)
    {
     viewInterface.updateSuccess(message);
    }

    @Override
    public void updateFailure(String message)
    {
     viewInterface.updateFailure(message);
    }
}
