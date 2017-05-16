package com.app.todo.login.presenter;

import android.content.Context;

import com.app.todo.login.interactor.LoginInteractorInterface;
import com.app.todo.login.interactor.LoginInteractor;
import com.app.todo.registration.model.UserModel;
import com.app.todo.login.ui.LoginActivityInterface;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter implements LoginPresenterInterface {

    LoginActivityInterface viewAnInterface;

    LoginInteractorInterface interactor;

    public LoginPresenter(Context context, LoginActivityInterface viewAnInterface) {
        this.viewAnInterface = viewAnInterface;
        interactor = new LoginInteractor(context, this);
    }

    @Override
    public void getLoginResponseFromFirebase(String email, String password) {
        interactor.getLoginResponsefromFirebase(email, password);

    }

    @Override
    public void loginSuccess(UserModel model) {
        viewAnInterface.loginSuccess(model);
    }

    @Override
    public void loginFailure(String message) {
        viewAnInterface.loginFailure(message);
    }

    @Override
    public void showDialog(String message) {
        viewAnInterface.showDialog(message);
    }

    @Override
    public void hideDialog() {
        viewAnInterface.hideDialog();
    }

    @Override
    public void getLoginResponseFromFacebook(CallbackManager callbackManager, LoginButton loginButton) {
        interactor.getLoginResponsefromFacebook(callbackManager,loginButton);
    }

    @Override
    public void fbLoginFailure(String message) {
        viewAnInterface.fbLoginFailure(message);
    }

    @Override
    public void fbLoginSuccess(JSONObject jsonObject, String userId, String message)
            throws JSONException {
        viewAnInterface.fbLoginSuccess(jsonObject, userId, message);
    }

    @Override
    public void handleGoogleSignInResult(GoogleSignInResult result) {
        interactor.handleGoogleSignInResult(result);
    }

    @Override
    public void googleLoginSuccess(GoogleSignInAccount account, String userId, String message) {
        viewAnInterface.googleLoginSuccess(account, userId, message);
    }

    @Override
    public void googleLoginFailure(String message) {
        viewAnInterface.googleLoginFailure(message);
    }
}