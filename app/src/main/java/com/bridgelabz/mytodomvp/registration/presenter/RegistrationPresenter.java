package com.bridgelabz.mytodomvp.registration.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.registration.interactor.RegistrationInteractor;
import com.bridgelabz.mytodomvp.registration.interactor.RegistrationInteractorInterface;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.bridgelabz.mytodomvp.registration.ui.RegistrationActivityInterface;

/**
 * Created by bridgeit on 7/5/17.
 */
public class RegistrationPresenter implements RegistrationPresenterInterface {
    RegistrationActivityInterface viewInterface;
    RegistrationInteractorInterface interactor;

    public RegistrationPresenter(Context context, RegistrationActivityInterface viewInterface) {
        this.viewInterface=viewInterface;


        interactor=new RegistrationInteractor(context,this);
    }

    @Override
    public void getRegister(UserModel model) {
    interactor.getRegisterResponse(model);
    }

    @Override
    public void registerSuccess(String message) {
    viewInterface.registrationSuccess(message);
    }

    @Override
    public void registerFailure(String message) {
     viewInterface.registraionFailure(message);
    }

    @Override
    public void showProgressDialogue(String message) {
    viewInterface.showProgressDailogue(message);
    }

    @Override
    public void hideProgressDialogue() {
     viewInterface.hideProgressDialogue();
    }
}
