package com.app.todo.homescreen.interactor;

import android.content.Context;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.HomeScreenPresenter;
import com.app.todo.homescreen.presenter.TodoNotesPresenter;
import com.app.todo.homescreen.presenter.TodoNotesPresenterInterface;
import com.app.todo.util.Connectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TodoNotesInteractor implements TodoNotesInteractorInterface {

    Context context;
    TodoNotesPresenterInterface presenter;

    /*Firebase objects*/
    FirebaseDatabase firebaseDatabase;
    DatabaseReference todoDataReference;

    public TodoNotesInteractor(Context context, TodoNotesPresenterInterface presenter) {
        this.context = context;
        this.presenter = presenter;
        firebaseDatabase = FirebaseDatabase.getInstance();
        todoDataReference = firebaseDatabase.getReference(Constant.key_firebase_todo);
    }

    @Override
    public void getTodoNoteFromServer(final String userId) {
        presenter.showDialog(context.getString(R.string.loading));
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
                    presenter.getNoteSuccess(noteList);
                    presenter.hideDialog();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    presenter.getNoteFailure(context.getString(R.string.some_error));
                    presenter.hideDialog();
                }
            });
        } else {
            presenter.getNoteFailure(context.getString(R.string.no_internet));
            presenter.hideDialog();
        }
    }

    @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos) {

        presenter.showDialog(context.getString(R.string.delete_todo_item_loading));
        if (Connectivity.isNetworkConnected(context)) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            int delete = 0;

            for (TodoItemModel model :
                    tempList) {
                todoDataReference.child(userId).child(model.getStartDate())
                        .child(String.valueOf(model.getNoteId())).setValue(model);
                delete = model.getNoteId() + 1;
            }
            if(delete != 0) {
                todoDataReference.child(userId).child(itemModel.getStartDate())
                        .child(String.valueOf(delete))
                        .removeValue();
            }else{
                todoDataReference.child(userId).child(itemModel.getStartDate())
                        .child(String.valueOf(itemModel.getNoteId()))
                        .removeValue();
            }

            presenter.deleteTodoModelSuccess(context.getString(R.string.delete_todo_item_success));
        } else {
            presenter.deleteTodoModelFailure(context.getString(R.string.no_internet));
        }

        presenter.hideDialog();
    }

    @Override
    public void moveToArchieve(TodoItemModel itemModel) {
        presenter.showDialog(context.getString(R.string.moving_to_archieve));

        if(Connectivity.isNetworkConnected(context)){
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            todoDataReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("archieved").setValue(true);

            presenter.moveSuccess(context.getString(R.string.moving_to_archieve_success));

        }else {
            presenter.moveFailure(context.getString(R.string.no_internet) + "\n"
                    + context.getString(R.string.moving_to_archieve_fail));
        }

        presenter.hideDialog();

    }

    @Override
    public void moveToNotes(TodoItemModel itemModel) {
        presenter.showDialog(context.getString(R.string.moving_to_note));

        if(Connectivity.isNetworkConnected(context)){
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            todoDataReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("archieved").setValue(false);

            presenter.moveSuccess(context.getString(R.string.moving_to_note_success));

        }else {
            presenter.moveFailure(context.getString(R.string.no_internet) + "\n"
                    + context.getString(R.string.moving_to_note_fail));
        }

        presenter.hideDialog();

    }
}