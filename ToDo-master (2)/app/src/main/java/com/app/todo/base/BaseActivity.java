package com.app.todo.base;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.app.todo.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by bridgeit on 26/3/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public abstract void initView();

    @Override
    public  void onConnectionFailed(@NonNull ConnectionResult connectionResult){

    }
}
