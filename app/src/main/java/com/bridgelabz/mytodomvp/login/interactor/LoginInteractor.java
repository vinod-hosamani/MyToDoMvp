package com.bridgelabz.mytodomvp.login.interactor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.login.presenter.LoginPresenterInterface;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.bridgelabz.mytodomvp.util.Connectivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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

/**
 * Created by bridgeit on 8/5/17.
 */
public class LoginInteractor implements LoginInteractorInterface {

    private static final String TAG="FBLOGIN RESULT";

    Context context;
    LoginPresenterInterface presenterInterface;

    UserModel model;

/*
    using firebaseobjects
*/
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String userId;


    public LoginInteractor(Context context,LoginPresenterInterface presenterInterface)
    {
        this.context=context;
        this.presenterInterface=presenterInterface;
        firebaseAuth=FirebaseAuth.getInstance();

    }
    @Override
    public void getLoginResponseFromFirebase(String email, String password)
    {
     presenterInterface.showProgressDailog("loading please wait..");
        if(Connectivity.isNetworkConnected(context)){

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                          if(task.isSuccessful()){
                              userId=task.getResult().getUser().getUid();
                              getUserData(userId);
                          }
                            else
                          {
                              presenterInterface.loginFailure("invalid email ro password");
                              presenterInterface.hideProgressDialog();
                          }
                        }
                    });
        }

    }

    @Override
    public void getLoginResposeFromFaceBook(CallbackManager callbackManager, LoginButton loginButton) {
    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.i(TAG,"onsuccess"+loginResult);
            handleFacebookAccessToken(loginResult.getAccessToken());
        }

        @Override
        public void onCancel()
        {
         Log.i(TAG,"onCancle ");
            presenterInterface.fbLoginFailure("you have cancelled the login process");
        }

        @Override
        public void onError(FacebookException error)
        {
            Log.i(TAG,"onErrror"+error);
            presenterInterface.fbLoginFailure(error.toString());
        }
    });
    }

    @Override
    public void handleGoogleSignInResult(GoogleSignInResult result)
    {
     presenterInterface.showProgressDailog("loading......");
        if(Connectivity.isNetworkConnected(context))
        {
            if(result.isSuccess())
            {
                final GoogleSignInAccount account=result.getSignInAccount();

                AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);

                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    String userId=firebaseAuth.getCurrentUser().getUid();
                                    presenterInterface.googleLoginSuccess(account,userId,"you have logged in your google account");

                                }
                                else
                                {
                                    presenterInterface.googleLoginFailure("you cant logged into your google account");
                                }
                            }
                        });
            }
            else
            {
                presenterInterface.googleLoginFailure("no internet connection");
            }
            presenterInterface.hideProgressDialog();
        }
    }

    public void getUserData(String userId)
    {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference(Constant.key_firebase_user);
        databaseReference.child(userId)
        .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                model=dataSnapshot.getValue(UserModel.class);
                presenterInterface.loginSuccess(model);
                presenterInterface.hideProgressDialog();

            }

            @Override
            public void onCancelled(DatabaseError error)
            {

                presenterInterface.loginFailure(error.getMessage());
                presenterInterface.hideProgressDialog();
            }
        });
    }

private void handleFacebookAccessToken(final AccessToken token)
{
    Log.d(TAG,"handleFacebookAccessToken "+token);
    AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
    firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG,"signInWithCredentials:success");
                        GraphRequest request=GraphRequest.newMeRequest(token,
                                new GraphRequest.GraphJSONObjectCallback()
                                {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response)
                                    {
                                       String userId=firebaseAuth.getCurrentUser().getUid();
                                        try{
                                            presenterInterface.fbLoginSuccess(object,userId,"loginwith fb successfull");
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                            presenterInterface.fbLoginFailure(e.getMessage());
                                        }
                                    }
                                });

                        Bundle parameters=new Bundle();
                        parameters.putString("fields","id,name,first_name,last_name,"+"email,gender,birthday,location");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                    else
                    {
                        presenterInterface.fbLoginFailure("cannot sign in ");
                    }
                }
            });
}

}
