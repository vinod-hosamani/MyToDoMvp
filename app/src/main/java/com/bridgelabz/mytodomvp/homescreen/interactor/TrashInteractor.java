package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.TrashPresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.TrashPresenterInterface;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bridgeit on 27/5/17.
 */
public class TrashInteractor implements TrashInteractorInterface
{
    Context context;
    TrashPresenterInterface presenter;
    private DatabaseReference toDoDataaReference;

    public TrashInteractor(Context context, TrashPresenter presenter)
    {
        this.context=context;
        this.presenter=presenter;
        toDoDataaReference= FirebaseDatabase.getInstance().getReference(Constant.key_firebase_todo);
    }


    @Override
    public void getNoteList(final String userId)
    {
        presenter.showProgressDialog("plese wait loading");
        if(Connectivity.isNetworkConnected(context))
        {
            toDoDataaReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<TodoItemModel> noteList=new ArrayList<>();
                    GenericTypeIndicator<ArrayList<TodoItemModel>> t=new
                            GenericTypeIndicator<ArrayList<TodoItemModel>>()
                            {
                            };
                    for(DataSnapshot obj:dataSnapshot.child(userId).getChildren())
                    {
                        List<TodoItemModel> li;
                        li = obj.getValue(t);
                        noteList.addAll(li);
                    }
                    noteList.removeAll(Collections.singleton(null));
                    presenter.getNoteListSuccess(noteList);
                    presenter.hideProgressDialog();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    presenter.getNoteListFailure("error");
                    presenter.hideProgressDialog();
                }
            });
        }
        else
        {
            presenter.getNoteListFailure("no internet connection");
            presenter.hideProgressDialog();
        }

    }
}
