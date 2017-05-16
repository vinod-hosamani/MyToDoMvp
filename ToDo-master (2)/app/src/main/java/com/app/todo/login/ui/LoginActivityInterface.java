package com.app.todo.login.ui;

import android.view.View;

import com.app.todo.registration.model.UserModel;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public interface LoginActivityInterface extends  View.OnClickListener{
    void loginSuccess(UserModel model);
    void loginFailure(String message);
    void showDialog(String message);
    void hideDialog();

    void fbLoginSuccess(JSONObject jsonObject, String userId, String message) throws JSONException;

    void fbLoginFailure(String message);

    void googleLoginSuccess(GoogleSignInAccount account, String userId, String message);

    void googleLoginFailure(String message);
}
