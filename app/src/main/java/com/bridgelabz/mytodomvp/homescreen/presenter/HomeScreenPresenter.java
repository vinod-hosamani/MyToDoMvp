package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.homescreen.interactor.HomeScreenInteractor;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivityInterface;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */
public class HomeScreenPresenter implements HomeScreenPresenterInterface {

    Context context;
    HomeScreenActivityInterface viewInterface;
    HomeScreenInteractor interactor;


    public HomeScreenPresenter(Context context,HomeScreenActivityInterface viewInterface){
        this.context=context;
        this.viewInterface=viewInterface;
        interactor=new HomeScreenInteractor(context,this);

    }
    @Override
    public void getTodoNoteFromServer(String userId) {

    }

    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList) {

    }

    @Override
    public void getNoteFailure(String message) {

    }

    @Override
    public void showProgressDailogue(String message) {

    }

    @Override
    public void hideProgressDailogue() {

    }

    @Override
    public void deleteTodoModelFailure(String message) {

    }

    @Override
    public void deleteTodoModelSuccess(String message) {

    }

    @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos) {

    }

    @Override
    public void moveToArchive(TodoItemModel itemModel) {

    }

    @Override
    public void moveToFailure(String message) {

    }

    @Override
    public void moveToSuccess(String message) {

    }

    @Override
    public void moveToNotes(TodoItemModel itemModel) {

    }
}
