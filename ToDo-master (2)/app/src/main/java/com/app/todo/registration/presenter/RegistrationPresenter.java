package com.app.todo.registration.presenter;

import android.content.Context;

import com.app.todo.registration.interactor.RegistrationInteractor;
import com.app.todo.registration.interactor.RegistrationInteractorInterface;
import com.app.todo.registration.model.UserModel;
import com.app.todo.registration.ui.RegistrationActivityInterface;

public class RegistrationPresenter implements RegistrationPresenterInterface {

    RegistrationActivityInterface viewInterface;
    RegistrationInteractorInterface interactor;

    public RegistrationPresenter(Context context, RegistrationActivityInterface viewInterface) {
        this.viewInterface = viewInterface;
        interactor = new RegistrationInteractor(context, this);
    }

    @Override
    public void getRegister(UserModel model) {
        interactor.getRegisterResponse(model);
    }

    @Override
    public void registerSuccess(String message) {
        viewInterface.registerSuccess(message);
    }

    @Override
    public void registerFailure(String message) {
        viewInterface.registerFailure(message);
    }

    @Override
    public void showProgressDialog(String message) {
        viewInterface.showProgressDialog(message);
    }

    @Override
    public void hideProgressDialog() {
        viewInterface.hideProgressDialog();
    }
}