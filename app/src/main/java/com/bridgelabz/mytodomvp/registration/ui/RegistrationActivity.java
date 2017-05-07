package com.bridgelabz.mytodomvp.registration.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.base.BaseActivity;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.login.ui.LoginActivity;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.bridgelabz.mytodomvp.registration.presenter.RegistrationPresenter;

/**
 * Created by bridgeit on 7/5/17.
 */
public class RegistrationActivity extends BaseActivity implements RegistrationActivityInterface {

    UserModel userModel;
    RegistrationPresenter presenter;

    AppCompatEditText nameEditText;
    AppCompatEditText emailEditText;
    AppCompatEditText mobileEditText;
    AppCompatEditText passwordEditText;
    AppCompatButton registrationButton;

    @Override
    public void initView() {
        presenter = new RegistrationPresenter(this, this);

        nameEditText = (AppCompatEditText) findViewById(R.id.editTextRegFullName);
        emailEditText = (AppCompatEditText) findViewById(R.id.editTextRegEmail);
        mobileEditText = (AppCompatEditText) findViewById(R.id.editTextRegMobileNo);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editTextRegPass);
        registrationButton = (AppCompatButton) findViewById(R.id.registrainButton);

        registrationButton.setOnClickListener(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
    }


    @Override
    public void registrationSuccess(String message) {
      if(!isFinishing())
          Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registraionFailure(String message) {
    if(!isFinishing())
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    ProgressDialog mProgressDialog;

    @Override
    public void showProgressDailogue(String message) {
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

    }

    @Override
    public void hideProgressDialogue(String message) {
       if(mProgressDialog!=null)
           mProgressDialog.hide();
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        switch (view.getId()) {
            case R.id.registrainButton:
                userModel = new UserModel();
                userModel.setFullname(nameEditText.getText().toString());
                userModel.setEmail(emailEditText.getText().toString());
                userModel.setMobile(mobileEditText.getText().toString());
                userModel.setPassword(passwordEditText.getText().toString());

                if (validationRegistration(userModel)) {
                    presenter.getRegister(userModel);
                    startActivity(intent);
                }
                break;
        }

    }

    private boolean validationRegistration(UserModel userModel) {

        boolean flag = true, toast = true;
        String mobilePattern = Constant.mobilepattern;
        String emailPattern = Constant.email_pattern;
        int passwordlen = userModel.getPassword().length();
        if (userModel.getFullname().length() == 0 || userModel.getMobile().length() == 0
                || userModel.getEmail().length() == 0 || userModel.getPassword().length() == 0) {

            Toast.makeText(getApplicationContext(), "plese enter all the field", Toast.LENGTH_SHORT).show();
            flag = flag && false;

        } else {

            if (userModel.getFullname().length() > 25) {
                Toast.makeText(getApplicationContext(), "enter correct name", Toast.LENGTH_SHORT).show();
                flag = flag && false;
            }

            if (userModel.getEmail().matches(emailPattern)) {
                //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                flag = flag && true;
            }
            if (toast) {
                if (!userModel.getEmail().matches(emailPattern)) {
                    emailEditText.requestFocus();
                    Toast.makeText(getApplicationContext(), "enter correct email id", Toast.LENGTH_SHORT).show();
                    toast = false;
                    flag = flag && false;
                }
            }

            if (userModel.getMobile().matches(mobilePattern)) {
                /*Toast.makeText(getApplicationContext(), "phone number is valid", Toast.LENGTH_SHORT).show();
                Log.v("Mobile Number Display",mobileno.getText().toString());
                Log.v("flag",b.toString());*/
                flag = flag && true;
            }

            if (toast) {
                if (!userModel.getMobile().matches(mobilePattern)) {
                    //Log.v("flag",b.toString());
                    //Log.v("Mobile Number Display",mobileno.getText().toString());
                    mobileEditText.requestFocus();
                    Toast.makeText(getApplicationContext(), "enter correct mobile  number", Toast.LENGTH_SHORT).show();
                    toast = false;
                    flag = flag && false;
                }
            }

            if (toast) {
                if (passwordlen < 8) {
                    passwordEditText.requestFocus();
                    Toast.makeText(getApplicationContext(), "invalid password", Toast.LENGTH_SHORT).show();
                    flag = flag && false;
                }
            }
        }
        return flag;
    }
}