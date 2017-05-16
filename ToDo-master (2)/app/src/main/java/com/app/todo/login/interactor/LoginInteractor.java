package com.app.todo.login.interactor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.login.presenter.LoginPresenterInterface;
import com.app.todo.registration.model.UserModel;
import com.app.todo.util.Connectivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginInteractor implements LoginInteractorInterface {

    private static final String TAG = "FBLOGIN RESULT";
    Context context;
    LoginPresenterInterface presenterInterface;

    UserModel model;

    /*Firebase objects*/
    FirebaseAuth userAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private String userId;

    public LoginInteractor(Context context, LoginPresenterInterface presenterInterface) {
        this.context = context;
        this.presenterInterface = presenterInterface;
        userAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void getLoginResponsefromFirebase(String email, String password) {
        presenterInterface.showDialog(context.getString(R.string.login_progress_dialog));
        if (Connectivity.isNetworkConnected(context)) {

            userAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userId = task.getResult().getUser().getUid();
                                getUserData(userId);
                            } else {
                                presenterInterface.loginFailure
                                        (context.getString(R.string.invalid_login_details));
                            }
                        }
                    });
        }else {
            presenterInterface.loginFailure(context.getString(R.string.no_internet));
        }
        presenterInterface.hideDialog();
    }

    public void getUserData(String userId) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Constant.key_firebase_user);
        databaseReference.child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        model = dataSnapshot.getValue(UserModel.class);
                        presenterInterface.loginSuccess(model);
                        presenterInterface.hideDialog();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        presenterInterface.loginFailure(error.getMessage());
                        presenterInterface.hideDialog();
                    }
                });
    }

    @Override
    public void getLoginResponsefromFacebook(CallbackManager callbackManager, LoginButton loginButton) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "onSuccess: " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: ");
                presenterInterface.fbLoginFailure(context.getString(R.string.fb_login_cancel));
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "onError: " + error);
                presenterInterface.fbLoginFailure(error.toString());
            }
        });
    }

    @Override
    public void handleGoogleSignInResult(GoogleSignInResult result) {
        presenterInterface.showDialog(context.getString(R.string.loading));
        if(Connectivity.isNetworkConnected(context)) {
            if (result.isSuccess()) {
                final GoogleSignInAccount account = result.getSignInAccount();

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                userAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = userAuth.getCurrentUser().getUid();
                                    presenterInterface.googleLoginSuccess
                                            (account, userId,context.getString(R.string.google_login_success));
                                } else {
                                    presenterInterface.googleLoginFailure
                                            (context.getString(R.string.google_login_error));
                                }
                            }
                        });

            } else {

            }
        }else{
            presenterInterface.googleLoginFailure(context.getString(R.string.no_internet));
        }
        presenterInterface.hideDialog();
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken: " + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        userAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredentials: success");
                            GraphRequest request = GraphRequest.newMeRequest(token,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object
                                                , GraphResponse response) {
                                            String userId = userAuth.getCurrentUser().getUid();
                                            try {
                                                presenterInterface.fbLoginSuccess(object, userId
                                                        , context.getString(R.string.fb_login_success));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                presenterInterface.fbLoginFailure(e.getMessage());
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id, name, first_name, last_name," +
                                    " email, gender, birthday, location");
                            request.setParameters(parameters);
                            request.executeAsync();
                        } else {
                            presenterInterface.fbLoginFailure
                                    (context.getString(R.string.fb_login_error));
                        }
                    }
                });
    }


}