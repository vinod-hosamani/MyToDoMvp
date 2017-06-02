package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.ArchivePresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.ArchivePresenterInterface;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.google.firebase.auth.FirebaseAuth;
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
                    noteList.removeAll(Collections.singleton(null));
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
    @Override
    public void goToTrash(TodoItemModel itemModel)
    {
        presenter.showProgressDialogue("moving to trash");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            toDoDataaReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("deleted").setValue(true);
            presenter.goToTrshSuccess("moved to trash");

        }
        else
        {
            presenter.goToTrashFailure("no internet connection");
        }
        presenter.hidePregressDialogu();
    }

    @Override
    public void goToNotes(TodoItemModel itemModel)
    {
            presenter.showProgressDialogue("moving to the notes");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
            toDoDataaReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("isArchived").setValue(false);
            presenter.goToNotesSuccess("moved notes successfully");
        }
        else
        {
            presenter.goToNotesFailure("no intentet connction");
        }
            presenter.hidePregressDialogu();
    }

    @Override
    public void notesAgainFromTrash(TodoItemModel itemModel)
    {
        presenter.showProgressDialogue("moving  to  again archive");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            toDoDataaReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("deleted").setValue(false);

            presenter.notesAgainFromTrashSuccess(" returned  successfully ");
            presenter.hidePregressDialogu();
        }
        else
        {
            presenter.notesAgainFromTrashFailure("no internet connectoin");
        }
        presenter.hidePregressDialogu();
    }

    @Override
    public void notesAgainFromNotes(TodoItemModel itemModel)
    {
        presenter.showProgressDialogue("moving to again archive");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            toDoDataaReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("isArchived").setValue(true);

            presenter.notesAgainFromNotesSuccess("returned succesfully");
            presenter.hidePregressDialogu();
        }
        else
        {
            presenter.notesAgainFromNotesFailure("no internet connection");
        }
        presenter.hidePregressDialogu();
    }
}