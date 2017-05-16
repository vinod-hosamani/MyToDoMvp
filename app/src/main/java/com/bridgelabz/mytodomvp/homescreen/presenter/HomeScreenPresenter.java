package com.bridgelabz.mytodomvp.homescreen.presenter;

import android.content.Context;
import android.net.Uri;

import com.bridgelabz.mytodomvp.homescreen.interactor.HomeScreenInteractor;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivityInterface;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */
public class HomeScreenPresenter implements HomeScreenPresenterInterface {

  //  Context context;
    HomeScreenActivityInterface viewInterface;
    HomeScreenInteractor interactor;


    public HomeScreenPresenter(Context context,HomeScreenActivityInterface viewInterface){
       // this.context=context;
        this.viewInterface=viewInterface;
        interactor=new HomeScreenInteractor(context,this);

    }
    @Override
    public void getTodoNoteFromServer(String userId)
    {
      interactor.getTodoNoteFromServer(userId);
    }

    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList)
    {
    viewInterface.getNoteSuccess(noteList);
    }

    @Override
    public void getNoteFailure(String message)
    {
viewInterface.getNoteFailure(message);
    }

    @Override
    public void showProgressDailogue(String message)
    {
   viewInterface.showProgressDialogue(message);
    }

    @Override
    public void hideProgressDailogue()
    {
  viewInterface.hideProgressDialogu();
    }

    @Override
    public void deleteTodoModelFailure(String message)
    {
viewInterface.deleteTodoModelFailure(message);
    }

    @Override
    public void deleteTodoModelSuccess(String message)
    {
viewInterface.deleteTodoModelSuccess(message);
    }

    @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos)
    {
interactor.deleteTodoModel(tempList,itemModel,pos);
    }

    @Override
    public void moveToArchive(TodoItemModel itemModel)
    {
interactor.motoToArchive(itemModel);
    }

    @Override
    public void moveToFailure(String message)
    {
viewInterface.moveToArchiveFailure(message);
    }

    @Override
    public void moveToSuccess(String message)
    {
viewInterface.moveToArchiveSuccess(message);
    }

    @Override
    public void moveToNotes(TodoItemModel itemModel)
    {
interactor.moveToNotes(itemModel);
    }

    @Override
    public void uploadProfilePic(String currentUserId, Uri selectedImage)
    {
        interactor.uploadProfilePic(currentUserId,selectedImage);
    }

    @Override
    public void uploadSuccess(Uri downloadUrl)
    {
      viewInterface.uploadSuccess(downloadUrl);
    }

    @Override
    public void uploadFailure(String message)
    {
       viewInterface.uploadFailure(message);
    }
}
