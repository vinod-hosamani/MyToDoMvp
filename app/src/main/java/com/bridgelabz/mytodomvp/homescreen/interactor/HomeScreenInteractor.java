package com.bridgelabz.mytodomvp.homescreen.interactor;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.HomeScreenPresenter;
import com.bridgelabz.mytodomvp.homescreen.presenter.HomeScreenPresenterInterface;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Collections;
import java.util.List;


/**
 * Created by bridgeit on 9/5/17.
 */
public class HomeScreenInteractor implements HomeScreenInteractorInterface
{
    Context context;
    HomeScreenPresenterInterface presenter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference todoDataReference;
    FirebaseStorage firebaseStorage;
    StorageReference profilePicReference;

    public HomeScreenInteractor(Context context, HomeScreenPresenter presenter)
    {
        this.context=context;
        this.presenter=presenter;
        firebaseDatabase=FirebaseDatabase.getInstance();
        todoDataReference=firebaseDatabase.getReference(Constant.key_firebase_todo);
        firebaseStorage = FirebaseStorage.getInstance();
        profilePicReference = firebaseStorage.getReference();
    }
    @Override
    public void getTodoNoteFromServer( final String userId)
    {
     presenter.showProgressDailogue("loading");
        if(Connectivity.isNetworkConnected(context))
        {
            todoDataReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    final List<TodoItemModel> noteList=new ArrayList<>();
                    GenericTypeIndicator<ArrayList<TodoItemModel>> t=new GenericTypeIndicator<ArrayList<TodoItemModel>>() {
                    };

                    for(DataSnapshot obj:dataSnapshot.child(userId).getChildren())
                    {
                        List<TodoItemModel> li;
                        li = obj.getValue(t);
                        noteList.addAll(li);
                    }
                    noteList.removeAll(Collections.singleton(null));
                    presenter.getNoteSuccess(noteList);
                    presenter.hideProgressDailogue();
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    presenter.getNoteFailure("some error");
                    presenter.hideProgressDailogue();
                }
            });
        }
        else
        {
            presenter.getNoteFailure(context.getString(R.string.no_internet));
            presenter.hideProgressDailogue();
        }
    }
    @Override
    public void uploadProfilePic(final String currentUserId, Uri selectedImage)
    {
        presenter.showProgressDailogue("upload_pro_pic");
        if(Connectivity.isNetworkConnected(context))
        {
            UploadTask task = profilePicReference.child(currentUserId)
                    .child("profilePic.jpeg").putFile(selectedImage);
            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    DatabaseReference userDatabase = firebaseDatabase
                            .getReference(Constant.key_firebase_user);

                    userDatabase.child(currentUserId).child("imageUrl")
                            .setValue(downloadUri.toString());
                    presenter.uploadSuccess(downloadUri);
                    presenter.hideProgressDailogue();
                }
            });
            task.addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    presenter.uploadFailure(e.getMessage());
                    presenter.hideProgressDailogue();
                }
            });
        }
        else
        {
            presenter.uploadFailure(context.getString(R.string.no_internet));
            presenter.hideProgressDailogue();
        }
    }
}
