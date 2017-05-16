package com.app.todo.registration.presenter;

import com.app.todo.registration.model.UserModel;

public interface RegistrationPresenterInterface {

    void getRegister(UserModel model);
    void registerSuccess(String message);
    void registerFailure(String message);
    void showProgressDialog(String message);
    void hideProgressDialog();
}