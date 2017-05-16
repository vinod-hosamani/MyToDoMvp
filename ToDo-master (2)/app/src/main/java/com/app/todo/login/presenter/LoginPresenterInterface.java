package com.app.todo.login.presenter;

import com.app.todo.registration.model.UserModel;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public interface LoginPresenterInterface {

    void getLoginResponseFromFirebase(String email, String password);

    void loginSuccess(UserModel model);
    void loginFailure(String message);
    void showDialog(String message);
    void hideDialog();

    void getLoginResponseFromFacebook(CallbackManager callbackManager, LoginButton loginButton);

    void fbLoginFailure(String message);

    void fbLoginSuccess(JSONObject jsonObject, String userId, String message) throws JSONException;

    void handleGoogleSignInResult(GoogleSignInResult result);

    void googleLoginSuccess(GoogleSignInAccount account, String userId, String message);

    void googleLoginFailure(String message);
}