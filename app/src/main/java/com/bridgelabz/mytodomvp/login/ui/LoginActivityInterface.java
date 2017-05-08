package com.bridgelabz.mytodomvp.login.ui;

import android.view.View;

import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bridgeit on 8/5/17.
 */
public interface LoginActivityInterface extends View.OnClickListener {
    void loginSuccess(UserModel model);
    void loginFailure(String message);
    void showPogressDailog(String message);
    void hideProgressDailog();

    void fbLoginSuccess(JSONObject jsonObject,String userId,String message) throws JSONException;
    void fbLoginFailure(String message);
    void  googleLoginSuccess(GoogleSignInAccount account,String userId,String message);
    void googleLoginFailure(String message);
}
