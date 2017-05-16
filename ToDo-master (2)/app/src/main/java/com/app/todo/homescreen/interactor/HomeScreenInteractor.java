package com.app.todo.homescreen.interactor;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.HomeScreenPresenter;
import com.app.todo.homescreen.presenter.HomeScreenPresenterInterface;
import com.app.todo.util.Connectivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 24/4/17.
 */

public class HomeScreenInteractor implements HomeScreenInteractorInterface {

    Context context;
    HomeScreenPresenterInterface presenter;

    /*Firebase objects*/
    FirebaseDatabase firebaseDatabase;
    DatabaseReference todoDataReference;

    FirebaseStorage firebaseStorage;
    StorageReference profilePicReference;

    public HomeScreenInteractor(Context context, HomeScreenPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        firebaseDatabase = FirebaseDatabase.getInstance();
        todoDataReference = firebaseDatabase.getReference(Constant.key_firebase_todo);

        firebaseStorage = FirebaseStorage.getInstance();
        profilePicReference = firebaseStorage.getReference();
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
            presenter.hideDialog();
        } else {
            presenter.deleteTodoModelFailure(context.getString(R.string.no_internet));
            presenter.hideDialog();
        }
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
            presenter.hideDialog();

        }else {
            presenter.moveFailure(context.getString(R.string.no_internet) + "\n"
                    + context.getString(R.string.moving_to_archieve_fail));
            presenter.hideDialog();
        }

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
            presenter.hideDialog();

        }else {
            presenter.moveFailure(context.getString(R.string.no_internet) + "\n"
                    + context.getString(R.string.moving_to_note_fail));
            presenter.hideDialog();
        }

    }

    @Override
    public void uploadProfilePic(final String currentUserId, Uri selectedImage) {
        presenter.showDialog(context.getString(R.string.upload_pro_pic));
        if(Connectivity.isNetworkConnected(context)) {
            UploadTask task = profilePicReference.child(currentUserId)
                    .child("profilePic.jpeg").putFile(selectedImage);

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    DatabaseReference userDatabase = firebaseDatabase
                            .getReference(Constant.key_firebase_user);

                    userDatabase.child(currentUserId).child("imageUrl")
                            .setValue(downloadUri.toString());

                    presenter.uploadSuccess(downloadUri);
                    presenter.hideDialog();
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    presenter.uploadFailure(e.getMessage());
                    presenter.hideDialog();
                }
            });
        }else {
            presenter.uploadFailure(context.getString(R.string.no_internet));
            presenter.hideDialog();
        }
    }
}