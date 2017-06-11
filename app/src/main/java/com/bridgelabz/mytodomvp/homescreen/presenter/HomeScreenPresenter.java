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
public class HomeScreenPresenter implements HomeScreenPresenterInterface
{

    HomeScreenActivityInterface viewInterface;
    HomeScreenInteractor interactor;


    public HomeScreenPresenter(Context context,HomeScreenActivityInterface viewInterface)
    {
        this.viewInterface=viewInterface;
        interactor=new HomeScreenInteractor(context,this);

    }
    @Override
    public void  getTodoNoteFromServer(String userId)
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
