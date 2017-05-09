package com.bridgelabz.mytodomvp.homescreen.ui.fragment;

import android.view.View;

/**
 * Created by bridgeit on 9/5/17.
 */
public interface AddToDoFragmentInterface extends View.OnClickListener {

    void addTodoSuccess(String message);
    void addTodoFailure(String message);
    void showProgressDailogue(String message);
    void hideProgressDialogue();
    void updateSuccess(String message);
    void updateFailure(String message);
}
