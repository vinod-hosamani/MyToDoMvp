package com.app.todo.homescreen.interactor;

import android.content.Context;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.ArchievedPresenter;
import com.app.todo.homescreen.presenter.ArchievedPresenterInterface;
import com.app.todo.util.Connectivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 3/5/17.
 */

public class ArchievedInteractor implements ArchievedInteractorInterface {

    Context context;
    ArchievedPresenterInterface presenter;
    private DatabaseReference todoDataReference;

    public ArchievedInteractor(Context context, ArchievedPresenter presenter) {
        this.context = context;
        this.presenter = presenter;

        todoDataReference = FirebaseDatabase.getInstance().getReference(Constant.key_firebase_todo);
    }


    @Override
    public void getNoteList(final String userId) {
        presenter.showProgressDialog(context.getString(R.string.loading));
        if (Connectivity.isNetworkConnected(context)) {

            todoDataReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<TodoItemModel> noteList = new ArrayList<>();
                    GenericTypeIndicator<ArrayList<TodoItemModel>> t = new GenericTypeIndicator<ArrayList<TodoItemModel>>() {
                    };
                    for (DataSnapshot obj : dataSnapshot.child(userId).getChildren()) {
                        List<TodoItemModel> li;
                        li = obj.getValue(t);
                        noteList.addAll(li);
                    }
                    presenter.getNoteListSuccess(noteList);
                    presenter.hideProgressDialog();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    presenter.getNoteListFailure(context.getString(R.string.some_error));
                    presenter.hideProgressDialog();
                }
            });
        } else {
            presenter.getNoteListFailure(context.getString(R.string.no_internet));
            presenter.hideProgressDialog();
        }
    }
}