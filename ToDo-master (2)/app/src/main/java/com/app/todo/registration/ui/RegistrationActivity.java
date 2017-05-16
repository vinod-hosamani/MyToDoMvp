package com.app.todo.registration.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.app.todo.R;
import com.app.todo.base.BaseActivity;
import com.app.todo.constants.Constant;
import com.app.todo.login.ui.LoginActivity;
import com.app.todo.registration.model.UserModel;
import com.app.todo.registration.presenter.RegistrationPresenter;
import com.app.todo.session.SessionManagement;

public class RegistrationActivity extends BaseActivity
        implements RegistrationActivityInterface {

    SessionManagement session;
    UserModel userModel;

    AppCompatTextView txtAlreadyAcc;
    AppCompatEditText fullNameEditText;
    AppCompatEditText emailEditText;
    AppCompatEditText mobileEditText;
    AppCompatEditText passwordEditText;
    AppCompatButton btnRegistration;

    RegistrationPresenter presenter;

    @Override
    public void initView() {

        presenter = new RegistrationPresenter(this, this);

        txtAlreadyAcc = (AppCompatTextView) findViewById(R.id.txtAlreadyAccount);

        fullNameEditText = (AppCompatEditText) findViewById(R.id.editViewFullName);
        emailEditText = (AppCompatEditText) findViewById(R.id.editViewEmail);
        mobileEditText = (AppCompatEditText) findViewById(R.id.editViewMobile);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editViewPass);

        btnRegistration = (AppCompatButton) findViewById(R.id.btnRegistration);

        btnRegistration.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        txtAlreadyAcc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);

        switch (view.getId()) {
            case R.id.btnRegistration:
                userModel = new UserModel();
                userModel.setFullname(fullNameEditText.getText().toString());
                userModel.setEmail(emailEditText.getText().toString());
                userModel.setPassword(passwordEditText.getText().toString());
                userModel.setMobile(mobileEditText.getText().toString());
                if(validateReg(userModel)){
                    presenter.getRegister(userModel);
                    startActivity(intent);
                }
                break;

            case R.id.txtAlreadyAccount:
                startActivity(intent);
                break;

        }
    }

    private boolean validateReg(UserModel userModel) {

        boolean flag = true, toast = true;
        String mobilePattern = Constant.mobilepattern;
        String emailPattern = Constant.email_pattern;
        int passwordlen = userModel.getPassword().length();
        if (userModel.getFullname().length() == 0 || userModel.getMobile().length() == 0
                || userModel.getEmail().length() ==
                0 || userModel.getPassword().length() == 0) {

            Toast.makeText(getApplicationContext(), R.string.empty_field, Toast.LENGTH_SHORT).show();
            flag = flag && false;

        } else {

            if (userModel.getFullname().length() > 25) {
                Toast.makeText(getApplicationContext(), R.string.invalid_name, Toast.LENGTH_SHORT).show();
                flag = flag && false;
            }

            if (userModel.getEmail().matches(emailPattern)) {
                //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                flag = flag && true;
            }
            if(toast) {
                if (!userModel.getEmail().matches(emailPattern)) {
                    emailEditText.requestFocus();
                    Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
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

            if(toast) {
                if (!userModel.getMobile().matches(mobilePattern)) {
                    //Log.v("flag",b.toString());
                    //Log.v("Mobile Number Display",mobileno.getText().toString());
                    mobileEditText.requestFocus();
                    Toast.makeText(getApplicationContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show();
                    toast = false;
                    flag = flag && false;
                }
            }

            if (toast) {
                if (passwordlen < 8) {
                    passwordEditText.requestFocus();
                    Toast.makeText(getApplicationContext(), R.string.invalid_pass, Toast.LENGTH_SHORT).show();
                    flag = flag && false;
                }
            }
        }
        return flag;
    }

    @Override
    public void registerSuccess(String message) {
        if (!isFinishing())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerFailure(String message) {
        if(!isFinishing())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog mProgressDialog;

    @Override
    public void showProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if(mProgressDialog != null)
            mProgressDialog.hide();
    }
}