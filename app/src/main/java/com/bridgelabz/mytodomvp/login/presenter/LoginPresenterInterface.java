package com.bridgelabz.mytodomvp.login.presenter;

import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bridgeit on 8/5/17.
 */
public interface LoginPresenterInterface
{


    void loginSuccess(UserModel model);
    void loginFailure(String message);
    void showProgressDailog(String message);
    void hideProgressDialog();

    void getLoginResponseFromFireBase(String email,String password);

    void getLoginResponseFromFacebook(CallbackManager callbackManager, LoginButton loginButton);
    void fbLoginSuccess(JSONObject jsonObject, String userId, String message) throws JSONException;
    void fbLoginFailure(String message);


    void handleGoogleSignInResult(GoogleSignInResult result);
    void googleLoginSuccess(GoogleSignInAccount account,String userId,String message);
    void googleLoginFailure(String message);
}
