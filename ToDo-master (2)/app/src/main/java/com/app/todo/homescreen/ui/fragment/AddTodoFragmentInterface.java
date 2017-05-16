package com.app.todo.homescreen.ui.fragment;

import android.view.View;

/**
 * Created by bridgeit on 22/4/17.
 */

public interface AddTodoFragmentInterface extends View.OnClickListener {

    void addTodoSuccess(String message);
    void addTodoFailure(String message);
    void showDialog(String message);
    void hideDialog();

    void updateSuccess(String message);

    void updateFailure(String message);
}