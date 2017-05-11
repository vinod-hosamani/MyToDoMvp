package com.bridgelabz.mytodomvp.registration.ui;

import android.view.View;

/**
 * Created by bridgeit on 7/5/17.
 */
public interface RegistrationActivityInterface  extends View.OnClickListener
{
    void registrationSuccess(String message);
    void registraionFailure(String message);
    void showProgressDailogue(String message);
    void hideProgressDialogue();
}
