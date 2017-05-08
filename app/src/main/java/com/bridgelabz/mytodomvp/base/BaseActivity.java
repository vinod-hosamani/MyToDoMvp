package com.bridgelabz.mytodomvp.base;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by bridgeit on 7/5/17.
 */
public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    public abstract void initView();

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
