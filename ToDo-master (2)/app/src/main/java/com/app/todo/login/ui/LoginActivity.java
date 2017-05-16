package com.app.todo.login.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.app.todo.R;
import com.app.todo.base.BaseActivity;
import com.app.todo.constants.Constant;
import com.app.todo.registration.model.UserModel;
import com.app.todo.login.presenter.LoginPresenter;
import com.app.todo.session.SessionManagement;
import com.app.todo.registration.ui.RegistrationActivity;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity  implements LoginActivityInterface {

    LoginPresenter presenter;

    AppCompatTextView txtCreateAcc;
    AppCompatEditText emailEditText;
    AppCompatEditText passwordEditText;
    AppCompatButton btnLogin;
    /*FB Login button*/
    LoginButton loginButton;

    String email;
    String password;

    SessionManagement session;

    /*Google Sign in*/
    public static GoogleSignInOptions googleSignInOptions;
    public static GoogleApiClient googleApiClient;
    SignInButton googleSignInButton;

    boolean isFbLogin;
    boolean isGoogleLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        /*For fb login*/
        callbackManager = CallbackManager.Factory.create();

        /*For google login*/
        googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleApiClient =  new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Override
    public void initView() {

        session = new SessionManagement(this);

        presenter = new LoginPresenter(this, this);

        txtCreateAcc = (AppCompatTextView) findViewById(R.id.txtCreateAccount);

        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);

        loginButton = (LoginButton) findViewById(R.id.fb_login_button);

        emailEditText = (AppCompatEditText) findViewById(R.id.editViewEmailLogin);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editViewPassLogin);

        txtCreateAcc.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        /*fb login button*/
        loginButton.setReadPermissions("public_profile","email");
        loginButton.setOnClickListener(this);

        /*Google sign in button*/
        googleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtCreateAccount:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.btnLogin:
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                presenter.getLoginResponseFromFirebase(email, password);
                break;

            case R.id.fb_login_button:
                presenter.getLoginResponseFromFacebook(callbackManager,loginButton);
                break;

            case R.id.google_sign_in_button:
                googleSignIn();
                break;
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, Constant.google_sign_in_req_code);
    }

    public void loginToSharedPreference(UserModel model) {
        isFbLogin = false;
        isGoogleLogin = false;
        session.put(model, isFbLogin, isGoogleLogin);
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginSuccess(UserModel model) {
        loginToSharedPreference(model);
    }

    @Override
    public void loginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;

    @Override
    public void showDialog(String message) {
        if(!isFinishing()){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if(!isFinishing() && progressDialog != null){
            progressDialog.dismiss();
        }
    }

    CallbackManager callbackManager;

    @Override
    public void fbLoginSuccess(JSONObject jsonObject, String userId, String message)
            throws JSONException {

        isFbLogin = true;
        UserModel model = new UserModel();
        if(!jsonObject.getString(Constant.key_fb_email).equals("")) {
            model.setEmail(jsonObject.getString(Constant.key_fb_email));
        }
        Toast.makeText(this, jsonObject.getString(Constant.key_fb_email), Toast.LENGTH_SHORT).show();
        model.setFullname(jsonObject.getString(Constant.key_fb_name));
        model.setMobile(jsonObject.getString(Constant.key_fb_id));
        model.setId(userId);
        model.setPassword("");
        session.put(model, isFbLogin, isGoogleLogin);

        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void fbLoginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void googleLoginSuccess(GoogleSignInAccount account, String userId, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        isGoogleLogin = true;

        UserModel model = new UserModel();
        model.setId(userId);
        model.setFullname(account.getDisplayName());
        model.setEmail(account.getEmail());
        model.setPassword("");
        model.setMobile(account.getPhotoUrl().toString());
        session.put(model, isFbLogin, isGoogleLogin);

        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void googleLoginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constant.google_sign_in_req_code){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.handleGoogleSignInResult(result);
        }
    }
}