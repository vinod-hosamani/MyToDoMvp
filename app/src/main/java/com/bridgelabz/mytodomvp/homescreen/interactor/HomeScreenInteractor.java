package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.HomeScreenPresenter;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    public void getTodoNoteFromServer( final String userId) {
     presenter.showProgressDailogue("loading");
        if(Connectivity.isNetworkConnected(context))
        {
            todoDataReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<TodoItemModel> noteList=new ArrayList<>();
                    GenericTypeIndicator<ArrayList<TodoItemModel>> t=new GenericTypeIndicator<ArrayList<TodoItemModel>>() {
                    };

                    for(DataSnapshot obj:dataSnapshot.child(userId).getChildren()){
                        List<TodoItemModel> li;
                        li = obj.getValue(t);
                        noteList.addAll(li);

                    }
                    presenter.getNoteSuccess(noteList);
                    presenter.hideProgressDailogue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                presenter.getNoteFailure("some error");
                    presenter.hideProgressDailogue();
                }
            });
        }
        else
        {
            presenter.getNoteFailure(context.getString(R.string.no_internet));
            presenter.hideProgressDailogue();
        }

    }

    @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos)
    {

    }

    @Override
    public void motoToArchive(TodoItemModel itemModel) {

    }

    @Override
    public void moveToNotes(TodoItemModel itemModel) {

    }
}
