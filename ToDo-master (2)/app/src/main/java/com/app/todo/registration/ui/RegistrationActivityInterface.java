package com.app.todo.registration.ui;

import android.view.View;

public interface RegistrationActivityInterface extends View.OnClickListener {
    void registerSuccess(String message);
    void registerFailure(String message);
    void showProgressDialog(String message);
    void hideProgressDialog();
}
