package com.bridgelabz.mytodomvp.registration.presenter;

import com.bridgelabz.mytodomvp.registration.model.UserModel;

/**
 * Created by bridgeit on 7/5/17.
 */
public interface RegistrationPresenterInterface
{
    void getRegister(UserModel model);

    void registerSuccess(String message);
    void registerFailure(String message);
    void showProgressDialogue(String message);
    void hideProgressDialogue();
}
