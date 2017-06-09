package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.TrashPresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.TrashPresenterInterface;
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
            toDoDataaReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
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
                public void onCancelled(DatabaseError databaseError)
                {
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

    @Override
    public void permanetDelete(List<TodoItemModel> tempList, TodoItemModel itemModel, int pos)
    {
        presenter.showProgressDialog("deleting permanent");
        if(Connectivity.isNetworkConnected(context))
        {
            String useId= FirebaseAuth.getInstance().getCurrentUser().getUid();
            int delete=0;
            for(TodoItemModel model:tempList)
            {
                toDoDataaReference.child(useId).child(model.getStartDate())
                        .child(String.valueOf(model.getNoteId())).setValue(model);
                delete=model.getNoteId()+1;
            }
            if(delete!=0)
            {
                toDoDataaReference.child(useId).child(itemModel.getStartDate())
                        .child(String.valueOf(delete))
                        .removeValue();
            }
            else
            {
                toDoDataaReference.child(useId).child(itemModel.getStartDate())
                        .child(String.valueOf(itemModel.getNoteId()))
                        .removeValue();
            }
            presenter.permanentDeleteSuccess(context.getString(R.string.delete_todo_item_success));
        }
        else
        {
            presenter.permanentDeleteFailure(context.getString(R.string.no_internet));
        }
            presenter.hideProgressDialog();
    }

    @Override
    public void moveToArchiveFromTrash(TodoItemModel itemModel)
    {
            presenter.showProgressDialog("moving to archive");
        if(Connectivity.isNetworkConnected(context))
        {
            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            toDoDataaReference.child(userId).child(itemModel.getStartDate())
                    .child(String.valueOf(itemModel.getNoteId()))
                    .child("deleted").setValue(false);
            presenter.moveToArchiveFromTrashSuccess("moved to archive");
            presenter.hideProgressDialog();
        }
        else
        {
            presenter.moveToArchvieFromTrashFailure("no internet connetion");
        }
            presenter.hideProgressDialog();
    }
}
