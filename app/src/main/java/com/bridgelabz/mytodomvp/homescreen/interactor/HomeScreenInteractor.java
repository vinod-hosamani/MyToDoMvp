package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.HomeScreenPresenter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by bridgeit on 9/5/17.
 */
public class HomeScreenInteractor implements HomeScreenInteractorInterface {

    Context context;
    HomeScreenPresenter presenter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference todoDataReference;



    public HomeScreenInteractor(Context context, HomeScreenPresenter presenter)
    {
        this.context=context;
        this.presenter=presenter;
        firebaseDatabase=FirebaseDatabase.getInstance();
        todoDataReference=firebaseDatabase.getReference(Constant.key_firebase_todo);
    }
    @Override
    public void getTodoNoteFromServer(String userId) {

    }

    @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos) {

    }

    @Override
    public void motoToArchive(TodoItemModel itemModel) {

    }

    @Override
    public void moveToNotes(TodoItemModel itemModel) {

    }
}
