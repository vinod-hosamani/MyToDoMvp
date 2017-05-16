package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.TodoNotesPresenterInterface;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.google.firebase.auth.FirebaseAuth;
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
public class TodoNotesInteractor implements TodoNotesInteractorInteraface {
    Context context;
    TodoNotesPresenterInterface presenter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference todoDataReference;

    public TodoNotesInteractor(Context context,TodoNotesPresenterInterface presenter)
    {
        this.context=context;
        this.presenter=presenter;
        firebaseDatabase=FirebaseDatabase.getInstance();
        todoDataReference=firebaseDatabase.getReference(Constant.key_firebase_todo);
    }
    @Override
    public void getTodoNoteFromServer( final  String userId) {
        presenter.showProgressDialog("loading");
        if(Connectivity.isNetworkConnected(context))
        {
            todoDataReference.addValueEventListener(new ValueEventListener() {
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
                        li=obj.getValue(t);
                        noteList.addAll(li);
                    }
                    presenter.getTodoNoteSuccess(noteList);
                    presenter.hideProgressDilogu();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    presenter.getNoteFailure("wrong");
                    presenter.hideProgressDilogu();
                }
            });
        }
        else
        {
            presenter.getNoteFailure("no internet connection");
            presenter.hideProgressDilogu();
        }


    }

    @Override
    public void deleteTodoModel(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos) {
     presenter.showProgressDialog("deleting ...");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
            int delete=0;
            for(TodoItemModel model:tempList)
            {
                todoDataReference.child(userId).child(model.getStartDate())
                        .child(String.valueOf(model.getNoteId())).setValue(model);
                delete=model.getNoteId()+1;

            }
            if(delete!=0)
            {
                todoDataReference.child(userId).child(itemModel.getStartDate())
                        .child(String.valueOf(delete))
                        .removeValue();

            }
            else
            {
                todoDataReference.child(userId).child(itemModel.getStartDate())
                        .child(String.valueOf(itemModel.getNoteId()))
                        .removeValue();
            }
            presenter.deleteTodoModelSuccess("you deleted ur note");
        }
        else
        {
            presenter.deletoTodoModelFailure("no intenet connection");
        }
         presenter.hideProgressDilogu();
    }

    @Override
    public void movetToArchive(TodoItemModel itemModel)
    {
       presenter.showProgressDialog("moving to archive");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            todoDataReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("arhcived").setValue(true);
            presenter.moveSuccess("moved to archvied");

        }
        else
        {
            presenter.moveFailure("no internet connection");
        }
        presenter.hideProgressDilogu();
    }

    @Override
    public void moveToNotes(TodoItemModel itemModel) {
  presenter.showProgressDialog("moving to the notes");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
            todoDataReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getStartDate()))
                    .child("archived").setValue(false);
            presenter.moveSuccess("moved notes successfully");
        }
        else
        {
            presenter.moveFailure("no intentet connction");
        }
        presenter.hideProgressDilogu();
    }
}
