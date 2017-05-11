package com.bridgelabz.mytodomvp.registration.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
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
public class RegistrationInteractor implements RegistrationInteractorInterface
{
    Context context;
    RegistrationPresenterInterface registrationPresenterInterface;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userId;

    public RegistrationInteractor(Context context, RegistrationPresenterInterface registrationPresenterInterface)
    {
        this.context=context;
        this.registrationPresenterInterface=registrationPresenterInterface;
    }


    @Override
    public void getRegisterResponse(final UserModel model)
    {
        registrationPresenterInterface.showProgressDialogue("registration in progress....");

        if(Connectivity.isNetworkConnected(context))
        {
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(model.getEmail(),model.getPassword())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                       userId=task.getResult().getUser().getUid();

                        model.setId(userId);
                        firebaseDatabase=FirebaseDatabase.getInstance();
                        databaseReference=firebaseDatabase.getReference(Constant.key_firebase_user);

                        databaseReference.child(userId).setValue(model);

                        registrationPresenterInterface.registerSuccess("registration completed successfullly");
                        registrationPresenterInterface.hideProgressDialogue();
                    }
                    else {
                        registrationPresenterInterface.registerFailure("registration failed");
                        registrationPresenterInterface.hideProgressDialogue();
                    }
                }
            });
        }

        else
        {
            registrationPresenterInterface.registerFailure("no internet connection");
        }
    }
}
