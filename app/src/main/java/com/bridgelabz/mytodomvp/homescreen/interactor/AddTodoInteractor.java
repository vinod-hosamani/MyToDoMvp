package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.AddTodoPresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.AddTodoPresenterInterface;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bridgeit on 10/5/17.
 */
public class AddTodoInteractor implements AddTodoInteractorInterface
{

    Context context;
    AddTodoPresenterInterface presenterInterface;
    TodoItemModel itemModel;

    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public AddTodoInteractor(Context context, AddTodoPresenter presenterInterface)
    {
        this.context=context;
        this.presenterInterface = presenterInterface;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Constant.key_firebase_todo);
    }
    @Override
    public void getResponseForAddTodoToServer(final TodoItemModel model, final String userId)
    {
      presenterInterface.showProgressDialogue("saving the notes");
        if(Connectivity.isNetworkConnected(context))
        {
            itemModel=model;
            databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(itemModel!=null)
                    {
                        int index=(int)dataSnapshot.child(userId).child(model.getStartDate()).getChildrenCount();
                        getIndex(index,userId,itemModel);

                        itemModel=null;

                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
        else
        {
            presenterInterface.addTodoFailure("no internet connection");
            presenterInterface.hideProgressDialogue();
        }
    }
    public void getIndex(int index,String userId,TodoItemModel model)
    {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {
            model.setNoteId(index);
            databaseReference.child(userId).child(model.getStartDate())
                    .child(String.valueOf(index)).setValue(model);
            presenterInterface.addTodoSuccess("notes added successfully");
            presenterInterface.hideProgressDialogue();
        }
        else
        {
            presenterInterface.addTodoFailure("notes to do failed");
            presenterInterface.hideProgressDialogue();
        }
    }

    @Override
    public void getResponseForUpdateTodoToServer(TodoItemModel model, String userId)
    {
       presenterInterface.showProgressDialogue("plese wait updating");
        if(Connectivity.isNetworkConnected(context))
        {
            databaseReference.child(userId).child(model.getStartDate()).child(String.valueOf(model.getNoteId())).setValue(model);
            presenterInterface.updateSuccess("updateSuccess");
        }
        else
        {
            presenterInterface.updateFailure("noted failed to update");

        }
        presenterInterface.hideProgressDialogue();
    }
}
