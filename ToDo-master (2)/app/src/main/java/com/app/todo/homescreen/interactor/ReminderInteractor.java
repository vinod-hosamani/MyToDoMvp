package com.app.todo.homescreen.interactor;

import android.content.Context;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.ReminderPresenter;
import com.app.todo.homescreen.presenter.ReminderPresenterInterface;
import com.app.todo.util.Connectivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by bridgeit on 8/5/17.
 */

public class ReminderInteractor implements ReminderInteractorInterface {

    Context context;
    ReminderPresenterInterface presenter;

    DatabaseReference databaseReference;

    public ReminderInteractor(Context context, ReminderPresenter presenter) {
        this.context = context;
        this.presenter = presenter;

        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.key_firebase_todo);
    }

    @Override
    public void getTodayReminderList(String userId) {
        presenter.showDialog(context.getString(R.string.loading));

        if (Connectivity.isNetworkConnected(context)) {
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<TodoItemModel> noteList = new ArrayList<>();
                    GenericTypeIndicator<ArrayList<TodoItemModel>> t = new GenericTypeIndicator<ArrayList<TodoItemModel>>() {
                    };
                    for (DataSnapshot obj :
                            dataSnapshot.getChildren()) {
                        List<TodoItemModel> li;
                        li = obj.getValue(t);
                        noteList.addAll(li);
                    }
                    presenter.getTodayReminderSuccess(noteList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    presenter.getTodayReminderFailure(context.getString(R.string.some_error));
                }
            });
        }else {
            presenter.getTodayReminderFailure(context.getString(R.string.no_internet));
        }
        presenter.hideDialog();
    }
}