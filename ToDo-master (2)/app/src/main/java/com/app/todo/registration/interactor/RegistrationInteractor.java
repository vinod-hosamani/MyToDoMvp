package com.app.todo.registration.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.registration.model.UserModel;
import com.app.todo.registration.presenter.RegistrationPresenterInterface;
import com.app.todo.registration.presenter.RegistrationPresenter;
import com.app.todo.session.SessionManagement;
import com.app.todo.util.Connectivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationInteractor implements RegistrationInteractorInterface {

    Context context;
    RegistrationPresenterInterface presenterInterface;

    SessionManagement session;

    /*Firebase objects*/
    private FirebaseAuth userAuth;
    private FirebaseDatabase userDatabase;
    private DatabaseReference userDatabaseRef;
    private String userId;

    public RegistrationInteractor(Context context, RegistrationPresenterInterface presenterInterfrace) {
        this.context = context;
        this.presenterInterface = presenterInterfrace;
    }

    @Override
    public void getRegisterResponse(final UserModel model) {
        presenterInterface.showProgressDialog(context.getString(R.string.register_progress_dialog));

       /* session = new SessionManagement(context);
        session.register(model);*/
        if (Connectivity.isNetworkConnected(context)) {
            userAuth = FirebaseAuth.getInstance();

            userAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                userId = task.getResult().getUser().getUid();

                                model.setId(userId);
                                userDatabase = FirebaseDatabase.getInstance();
                                userDatabaseRef = userDatabase.getReference(Constant.key_firebase_user);

                                userDatabaseRef.child(userId).setValue(model);

                                presenterInterface.registerSuccess(context.getString(R.string.register_success));
                                presenterInterface.hideProgressDialog();
                            } else {
                                presenterInterface.registerFailure(context.getString(R.string.register_failed));
                                presenterInterface.hideProgressDialog();
                            }
                        }
                    });
        }else{
            presenterInterface.registerFailure(context.getString(R.string.no_internet));
        }
    }
}