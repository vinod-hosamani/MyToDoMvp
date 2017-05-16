package com.app.todo.homescreen.interactor;

import android.content.Context;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.AddTodoPresenter;
import com.app.todo.homescreen.presenter.AddTodoPresenterInterface;
import com.app.todo.util.Connectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bridgeit on 22/4/17.
 */

public class AddTodoInteractor implements AddTodoInteractorInterface {

    Context context;
    AddTodoPresenterInterface presenterInterface;
    TodoItemModel itemModel;

    /*Firebase objects*/
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public AddTodoInteractor(Context context, AddTodoPresenter presenterInterface) {
        this.context = context;
        this.presenterInterface = presenterInterface;

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Constant.key_firebase_todo);
    }



    @Override
    public void getResponseForAddTodoToServer(final TodoItemModel model, final String userId) {
        presenterInterface.showDialog(context.getString(R.string.note_saving));
        if(Connectivity.isNetworkConnected(context)){
            itemModel = model;

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(itemModel != null) {
                        int index = (int) dataSnapshot.child(userId)
                                .child(model.getStartDate()).getChildrenCount();
                        getIndex(index, userId, itemModel);
                        itemModel = null;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else {
            presenterInterface.addTodoFailure(context.getString(R.string.no_internet));
            presenterInterface.hideDialog();
        }
    }

    private void getIndex(int index, String userId, TodoItemModel model) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            model.setNoteId(index);
            databaseReference.child(userId).child(model.getStartDate())
                    .child(String.valueOf(index)).setValue(model);
            presenterInterface.addTodoSuccess(context.getString(R.string.addTodo_success));
            presenterInterface.hideDialog();
        }else {
            presenterInterface.addTodoFailure(context.getString(R.string.addTodo_failure));
            presenterInterface.hideDialog();
        }

    }

    @Override
    public void getResponseForUpdateTodoToServer(TodoItemModel model, String userId) {
        presenterInterface.showDialog(context.getString(R.string.updating));

        if(Connectivity.isNetworkConnected(context)) {
            databaseReference.child(userId).child(model.getStartDate())
                    .child(String.valueOf(model.getNoteId()))
                    .setValue(model);
            presenterInterface.updateSuccess(context.getString(R.string.update_success));
        }else {
            presenterInterface.updateFailure(context.getString(R.string.no_internet) + "\n"+
            context.getString(R.string.update_failure));
        }
        presenterInterface.hideDialog();
    }
}