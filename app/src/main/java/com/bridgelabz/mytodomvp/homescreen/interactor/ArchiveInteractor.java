package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.ArchivePresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.ArchivePresenterInterface;
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
 * Created by bridgeit on 11/5/17.
 */
public class ArchiveInteractor implements ArchiveInteractorInterfacce
{
    Context context;
    ArchivePresenterInterface presenter;
    private DatabaseReference toDoDataaReference;

    public ArchiveInteractor(Context context, ArchivePresenter presenter)
    {
        this.context=context;
        this.presenter=presenter;
        toDoDataaReference= FirebaseDatabase.getInstance().getReference(Constant.key_firebase_todo);
    }
    @Override
    public void getNoteList( final  String userId)
    {
        presenter.showProgressDialogue("plese wait loading ");
        if (Connectivity.isNetworkConnected(context))
        {
            toDoDataaReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    final List<TodoItemModel> noteList = new ArrayList<>();
                    GenericTypeIndicator<ArrayList<TodoItemModel>> t = new
                            GenericTypeIndicator<ArrayList<TodoItemModel>>()
                    {
                    };
                    for (DataSnapshot obj : dataSnapshot.child(userId).getChildren())
                    {
                        List<TodoItemModel> li;
                        li = obj.getValue(t);
                        noteList.addAll(li);
                    }
                    presenter.getNoteListSuccess(noteList);
                    presenter.hidePregressDialogu();
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    presenter.getNoteListFailure("error");
                    presenter.hidePregressDialogu();
                }
            });
        }
        else
        {
            presenter.getNoteListFailure("no internet connctoin");
            presenter.hidePregressDialogu();
        }
    }
}