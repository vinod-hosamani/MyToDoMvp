package com.app.todo.homescreen.ui.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.todo.R;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.AddTodoPresenter;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.app.todo.registration.model.UserModel;
import com.app.todo.session.SessionManagement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTodoFragment extends Fragment implements AddTodoFragmentInterface{

    AppCompatEditText editTextTitle;
    AppCompatEditText editTextNote;
    AppCompatTextView textTextReminder;
    AppCompatButton btnSave;

    TodoItemModel model;
    HomeScreenActivity homeScreenActivity;

    SessionManagement session;

    public static boolean add = true;
    public static int edit_pos;

    AddTodoPresenter presenter;

    public AddTodoFragment(HomeScreenActivity context)
    {
        this.homeScreenActivity=context;
        /*this.database = DatabaseHandler.getInstance(context);*/
        presenter = new AddTodoPresenter(context, this);
        session = new SessionManagement(context);
    }

    public void initView(View view)
    {

        editTextTitle = (AppCompatEditText) view.findViewById(R.id.editViewTodoTitle);
        editTextNote = (AppCompatEditText) view.findViewById(R.id.editViewTodoItem);
        textTextReminder = (AppCompatTextView) view.findViewById(R.id.textViewReminder);
        btnSave = (AppCompatButton) view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_add_todo_item, container, false);
        initView(view);
        Bundle args = getArguments();
        if(args != null){
            editTextTitle.setText(args.getString(Constant.key_title));
            editTextNote.setText(args.getString(Constant.key_note));
            textTextReminder.setText(args.getString(Constant.key_reminder));
            btnSave.setText(R.string.update_btn_text);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                if(add)
                    saveDataToAdapter();
                else
                    editTodoItem(edit_pos);
                break;
        }
    }

    private void editTodoItem(int pos)
    {
        UserModel userModel = session.getUserDetails();
        model = new TodoItemModel();

        model.setTitle(editTextTitle.getText().toString());
        model.setNote(editTextNote.getText().toString());
        model.setReminderDate(textTextReminder.getText().toString());
        model.setArchieved(false);
        Bundle bundle = getArguments();
        model.setNoteId(bundle.getInt(Constant.key_note_id));
        model.setStartDate(bundle.getString(Constant.key_startDate));
        /*database.updateTodo(model);*/
        presenter.getResponseForUpdateTodoToServer(model, userModel.getId());

        /*homeScreenActivity.updateAdapter(pos, model);*/
        homeScreenActivity.addTodoFab.setVisibility(View.VISIBLE);
        homeScreenActivity.setTitle(Constant.note_title);
        homeScreenActivity.getSupportFragmentManager().popBackStackImmediate();
    }

    public void saveDataToAdapter()
    {
        UserModel userModel = session.getUserDetails();

        model = new TodoItemModel();
        model.setReminderDate(textTextReminder.getText().toString());
        model.setTitle(editTextTitle.getText().toString());
        model.setNote(editTextNote.getText().toString());
        model.setArchieved(false);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(getContext()
                .getString(R.string.date_formate));
        String currentDate = format.format(date.getTime());
        model.setStartDate(currentDate);
        presenter.getResponseForAddTodoToServer(model, userModel.getId());

        /*database.addTodo(model);*/
        /*homeScreenActivity.updateAdapter(model);*/

        /*Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT);*/
        homeScreenActivity.addTodoFab.setVisibility(View.VISIBLE);
        homeScreenActivity.getSupportFragmentManager().popBackStackImmediate();
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {

        String myFormat = getContext().getString(R.string.date_formate); //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        textTextReminder.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public void addTodoSuccess(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addTodoFailure(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSuccess(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFailure(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;

    @Override
    public void showDialog(String message) {
        if (!homeScreenActivity.isFinishing()){
            progressDialog = new ProgressDialog(homeScreenActivity);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if(!homeScreenActivity.isFinishing() && progressDialog != null)
            progressDialog.dismiss();
    }

    Menu menu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_add_todo_fragment, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.add_reminder)
        {
            new DatePickerDialog(homeScreenActivity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            return true;
        }
        else if (item.getItemId() == R.id.action_logout)
        {

        }

        return super.onOptionsItemSelected(item);
    }
}