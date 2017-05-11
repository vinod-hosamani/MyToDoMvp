package com.bridgelabz.mytodomvp.registration.presenter;

import android.content.Context;

import com.bridgelabz.mytodomvp.registration.interactor.RegistrationInteractor;
import com.bridgelabz.mytodomvp.registration.interactor.RegistrationInteractorInterface;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.bridgelabz.mytodomvp.registration.ui.RegistrationActivityInterface;

/**
 * Created by bridgeit on 7/5/17.
 */
public class RegistrationPresenter implements RegistrationPresenterInterface
{
    RegistrationActivityInterface registrationActivityInterface;
    RegistrationInteractorInterface registrationInteractorInterface;

    public RegistrationPresenter(Context context, RegistrationActivityInterface registrationActivityInterface)
    {
        this.registrationActivityInterface=registrationActivityInterface;
        registrationInteractorInterface=new RegistrationInteractor(context,this);
    }

    @Override
    public void getRegister(UserModel model)
    {
        registrationInteractorInterface.getRegisterResponse(model);
    }

    @Override
    public void registerSuccess(String message)
    {
        registrationActivityInterface.registrationSuccess(message);
    }

    @Override
    public void registerFailure(String message)
    {
        registrationActivityInterface.registraionFailure(message);
    }

    @Override
    public void showProgressDialogue(String message)
    {
        registrationActivityInterface.showProgressDailogue(message);
    }

    @Override
    public void hideProgressDialogue()
    {
        registrationActivityInterface.hideProgressDialogue();

    }
}
