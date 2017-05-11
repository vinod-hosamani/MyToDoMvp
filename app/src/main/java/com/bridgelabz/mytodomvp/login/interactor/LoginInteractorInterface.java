package com.bridgelabz.mytodomvp.login.interactor;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/**
 * Created by bridgeit on 8/5/17.
 */
public interface LoginInteractorInterface
{
    void getLoginResponseFromFirebase(String email,String password);
    void getLoginResposeFromFaceBook(CallbackManager callbackManager, LoginButton loginButton);
    void handleGoogleSignInResult(GoogleSignInResult result);
}
