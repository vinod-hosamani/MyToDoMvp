package com.bridgelabz.mytodomvp.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.bridgelabz.mytodomvp.login.ui.LoginActivity;
import com.bridgelabz.mytodomvp.session.SessionManagement;

/**
 * Created by bridgeit on 13/5/17.
 */
public class SplashScreenAcitivity extends AppCompatActivity
{
    SessionManagement sessionManagement;
    public static final int TimeOUt=1111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        sessionManagement=new SessionManagement(this);
        if(sessionManagement.isLoggedIn())
        {
            Intent intent=new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();

        }
        else {
            callSplashScreen();
        }

    }
    private void callSplashScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                Intent i = new Intent(SplashScreenAcitivity.this ,LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, TimeOUt);
    }
}