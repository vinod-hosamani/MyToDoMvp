package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.ReminderPresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.ReminderPresenterIterface;
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
 * Created by bridgeit on 15/5/17.
 */
public class ReminderInteractor implements ReminderInteractorInterface {
    Context context;
    ReminderPresenterIterface presenter;
    DatabaseReference databaseReference;

    public ReminderInteractor(Context context, ReminderPresenter presenter)
    {
        this.context=context;
        this.presenter=presenter;
        databaseReference= FirebaseDatabase.getInstance().getReference(Constant.key_firebase_todo);
    }



    @Override
    public void getTodayReminderList(String userId) {
        presenter.showProgressDilogu("loading");
        if(Connectivity.isNetworkConnected(context))
        {
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<TodoItemModel> noteList = new ArrayList<>();
                    GenericTypeIndicator<ArrayList<TodoItemModel>> t = new
                            GenericTypeIndicator<ArrayList<TodoItemModel>>() {
                            };
                    for (DataSnapshot obj : dataSnapshot.getChildren()) {
                        List<TodoItemModel> li;
                        li = obj.getValue(t);
                        noteList.addAll(li);
                    }
                    presenter.getTodayReminderSuccess(noteList);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    presenter.getTodayReminderFailure("some Erro");
                }
            });
        }
        else
        {
            presenter.getTodayReminderFailure("no internet connection");
        }
        presenter.hideProgressDialgu();
    }
}
