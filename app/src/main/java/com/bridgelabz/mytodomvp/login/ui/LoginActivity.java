package com.bridgelabz.mytodomvp.login.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.base.BaseActivity;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.bridgelabz.mytodomvp.login.presenter.LoginPresenter;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.bridgelabz.mytodomvp.registration.ui.RegistrationActivity;
import com.bridgelabz.mytodomvp.session.SessionManagement;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bridgeit on 7/5/17.
 */
public class LoginActivity extends BaseActivity implements LoginActivityInterface {

    LoginPresenter presenter;

    AppCompatEditText emailEditText;
    AppCompatEditText passwordEditText;
    AppCompatButton loginButton;
    AppCompatTextView createAccount;

    LoginButton facebookLoginBtn;
    SignInButton googleSignInBtn;

    String email,password;

    SessionManagement session;

    public GoogleSignInOptions googleSignInOptions;
    public GoogleApiClient googleApiClient;

    boolean isFbLogin;
    boolean isGoogleLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        callbackManager= CallbackManager.Factory.create();

       /* for google login*/
        googleSignInOptions=new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();


    }

    @Override
    public void loginSuccess(UserModel model)
    {
      loginToSharedPreference(model);
    }

    @Override
    public void loginFailure(String message) {
     Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;

    @Override
    public void showPogressDailog(String message) {
     if(!isFinishing())
     {
         progressDialog=new ProgressDialog(this);
         progressDialog.setMessage(message);
         progressDialog.show();
     }
    }

    @Override
    public void hideProgressDailog()
    {
     if(!isFinishing() && progressDialog!=null){
         progressDialog.dismiss();
     }
    }

    CallbackManager callbackManager;
    @Override
    public void fbLoginSuccess(JSONObject jsonObject, String userId, String message)throws JSONException
    {
        isFbLogin=true;
        UserModel model=new UserModel();
        if(!jsonObject.getString("email").equals(""))
        {
            model.setEmail(jsonObject.getString("email"));
        }
        Toast.makeText(this,jsonObject.getString("email"),Toast.LENGTH_SHORT).show();
        model.setFullname(jsonObject.getString(Constant.key_fb_name));
        model.setMobile(jsonObject.getString(Constant.key_fb_id));
        model.setId(userId);
        model.setPassword("");
        session.put(model,isFbLogin,isGoogleLogin);

        Intent intent=new Intent(this,HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void fbLoginFailure(String message)
    {

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void googleLoginSuccess(GoogleSignInAccount account, String userId, String message)
    {
     Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        isGoogleLogin=true;

        UserModel model=new UserModel();
        model.setId(userId);
        model.setFullname(account.getDisplayName());
        model.setEmail(account.getEmail());
        model.setPassword("");
        model.setMobile(account.getPhotoUrl().toString());
        session.put(model,isFbLogin,isGoogleLogin);

        Intent intent=new Intent(this,HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void googleLoginFailure(String message)
    {
     Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.textViewLogCreateAccount:
                Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonLogLogin:
                email=emailEditText.getText().toString();
                password=passwordEditText.getText().toString();
                presenter.getLoginResponseFromFireBase(email,password);
                break;

            case R.id.facebookLoginButton:
                presenter.getLoginResponseFromFacebook(callbackManager,facebookLoginBtn);
                break;
            case R.id.googleSignInButton:
                googleSignIn();
                break;
        }

    }

    private void googleSignIn()
    {
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,Constant.google_sign_in_req_code);
    }

    public void loginToSharedPreference(UserModel model)
    {
        isFbLogin=false;
        isGoogleLogin=false;
        session.put(model,isFbLogin,isGoogleLogin);
        Intent intent=new Intent(this,HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void initView()
    {
        session=new SessionManagement(this);
        presenter=new LoginPresenter(this,this);
        createAccount=(AppCompatTextView)findViewById(R.id.textViewLogCreateAccount);
        loginButton=(AppCompatButton)findViewById(R.id.buttonLogLogin);

        facebookLoginBtn=(LoginButton)findViewById(R.id.facebookLoginButton);
        emailEditText=(AppCompatEditText)findViewById(R.id.editTextLogEmail);
        passwordEditText=(AppCompatEditText)findViewById(R.id.editTextLogPass);

        createAccount.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        /*facebook login button*/

        facebookLoginBtn.setReadPermissions("public_profile","email");
        facebookLoginBtn.setOnClickListener(this);

      /*  google sign in button*/

        googleSignInBtn=(SignInButton)findViewById(R.id.googleSignInButton);
        googleSignInBtn.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        if(requestCode==Constant.google_sign_in_req_code)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.handleGoogleSignInResult(result);
        }
    }
}
