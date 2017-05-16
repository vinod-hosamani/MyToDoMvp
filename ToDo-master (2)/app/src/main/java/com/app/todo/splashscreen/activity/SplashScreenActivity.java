package com.app.todo.splashscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.session.SessionManagement;
import com.app.todo.login.ui.LoginActivity;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;

public class SplashScreenActivity extends AppCompatActivity {

    SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        session = new SessionManagement(this);
        if(session.isLoggedIn()) {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        } else {
            callSplashScreen();
        }
    }

    private void callSplashScreen() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, Constant.splashTimeOut);
    }
}