package com.app.todo.login.interactor;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public interface LoginInteractorInterface {
    void getLoginResponsefromFirebase(String email, String password);

    void getLoginResponsefromFacebook(CallbackManager callbackManager, LoginButton loginButton);

    void handleGoogleSignInResult(GoogleSignInResult result);
}