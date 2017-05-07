package com.bridgelabz.mytodomvp.registration.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.bridgelabz.mytodomvp.registration.presenter.RegistrationPresenter;
import com.bridgelabz.mytodomvp.registration.presenter.RegistrationPresenterInterface;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bridgeit on 7/5/17.
 */
public class RegistrationInteractor implements RegistrationInteractorInterface {
    Context context;
    RegistrationPresenterInterface presenterInterface;

    private FirebaseAuth userAuth;
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseRef;
    private String userId;

    public RegistrationInteractor(Context context, RegistrationPresenter presenterInterface) {
        this.context=context;
        this.presenterInterface=presenterInterface;
    }


    @Override
    public void getRegisterResponse(final UserModel model) {
    presenterInterface.showProgressDialogue("registration in progress....");

        if(Connectivity.isNetworkConnected(context)){
            userAuth=FirebaseAuth.getInstance();

            userAuth.createUserWithEmailAndPassword(model.getEmail(),model.getPassword())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                       userId=task.getResult().getUser().getUid();

                        model.setId(userId);
                        userDatabase=FirebaseDatabase.getInstance();
                        userDatabaseRef=userDatabase.getReference(Constant.key_firebase_user);

                        userDatabaseRef.child(userId).setValue(model);

                        presenterInterface.registerSuccess("registration completed successfullly");
                        presenterInterface.hideProgressDialogue();
                    }
                    else {
                        presenterInterface.registerFailure("registration failed");
                        presenterInterface.hideProgressDialogue();
                    }
                }
            });
        }
        else {
            presenterInterface.registerFailure("no internet connection");
        }
    }
}
