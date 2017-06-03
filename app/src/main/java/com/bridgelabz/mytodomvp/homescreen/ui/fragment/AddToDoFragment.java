package com.bridgelabz.mytodomvp.homescreen.ui.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.AddTodoPresenter;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.bridgelabz.mytodomvp.registration.model.UserModel;
import com.bridgelabz.mytodomvp.session.SessionManagement;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import android.app.Fragment;

/**
 * Created by bridgeit on 9/5/17.
 */
public class AddToDoFragment extends Fragment implements AddToDoFragmentInterface
{
    HomeScreenActivity homeScreenActivity;

    SessionManagement session;
    AddTodoPresenter presenter;

    AppCompatEditText editTextTitle;
    AppCompatEditText editTextNotes;
    AppCompatTextView textViewReminder;
    int id;
    AppCompatButton buttonSave;

    public static boolean add=true;
    public static int editposition;

    TodoItemModel model;
    private int DIALOG_ID=10;
    private LinearLayout linearlayout;
    public String newcolor;

    public AddToDoFragment(HomeScreenActivity  context)
    {
        this.homeScreenActivity=context;
        presenter = new AddTodoPresenter(context, this);
        session = new SessionManagement(context);
    }

    public void  initView(View view)
    {
        editTextTitle=(AppCompatEditText)view.findViewById(R.id.editTextToDoTitle);
        editTextNotes=(AppCompatEditText)view.findViewById(R.id.editTextToDoDescription);
        textViewReminder=(AppCompatTextView)view.findViewById(R.id.textViewReminder);
        buttonSave=(AppCompatButton)view.findViewById(R.id.btnsave);

        buttonSave.setOnClickListener(this);

        linearlayout=(LinearLayout) view.findViewById(R.id.layout_update_color);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        View view=inflater.inflate(R.layout.fragment_add_to_item,container,false);
        initView(view);
        Bundle arguments=getArguments();
        if(arguments!=null)
        {
            editTextTitle.setText(arguments.getString(Constant.key_title));
            editTextNotes.setText(arguments.getString(Constant.key_note));
            textViewReminder.setText(arguments.getString(Constant.key_reminder));
            id=arguments.getInt(Constant.key_note_id);
            buttonSave.setText("update");
        }
        return view;

    }
    @Override
    public void addTodoSuccess(String message)
    {
        Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void addTodoFailure(String message)
    {
        Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }
    ProgressDialog progressDialog;
    @Override
    public void showProgressDailogue(String message)
    {
        if(!homeScreenActivity.isFinishing())
        {
            progressDialog=new ProgressDialog(homeScreenActivity);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }
    @Override
    public void hideProgressDialogue()
    {
     if(!homeScreenActivity.isFinishing() && progressDialog!=null)
     {
         progressDialog.dismiss();
     }
    }

    @Override
    public void updateSuccess(String message)
    {
      Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFailure(String message)
    {
      Toast.makeText(homeScreenActivity,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnsave:
                if(add)
                    savaDataAdapter();
                else
                    editTodoItem(editposition);
                break;
        }
    }
    public void editTodoItem(int position)
    {
        UserModel usermodel=session.getUserDetails();
        model=new TodoItemModel();
       // model.setNoteId(id);
        model.setTitle(editTextTitle.getText().toString());
        model.setNote(editTextNotes.getText().toString());
        model.setReminderDate(textViewReminder.getText().toString());
        model.setIsArchived(false);
        model.setColor(newcolor);

        Bundle bundle=getArguments();
        model.setNoteId(bundle.getInt(Constant.key_note_id));
        model.setStartDate(bundle.getString(Constant.key_startDate));

        presenter.getResponseForUpdateTodoToServer(model,usermodel.getId());
        homeScreenActivity.updateAdapter(position,model);
        homeScreenActivity.addTodoFab.setVisibility(View.VISIBLE);
        homeScreenActivity.getSupportFragmentManager().popBackStackImmediate();
    }

    public void savaDataAdapter()
    {
        UserModel userModel=session.getUserDetails();
        model=new TodoItemModel();
        model.setTitle(editTextTitle.getText().toString());
        model.setNote(editTextNotes.getText().toString());
        model.setReminderDate(textViewReminder.getText().toString());
        model.setIsArchived(false);
        model.setDeleted(false);
        model.setColor(newcolor);

        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("MMMM dd,yyyy");
        String currentDate=format.format(date.getTime());
        model.setStartDate(currentDate);
        presenter.getResponseForAddTodoToServer(model,userModel.getId());
        homeScreenActivity.addTodoFab.setVisibility(View.VISIBLE);
        homeScreenActivity.getSupportFragmentManager().popBackStackImmediate();
}
    Calendar myCalender= Calendar.getInstance();
    private void updateLabe()
    {
        String myFormat="MMMM dd,yyyy";
        SimpleDateFormat sdf=new SimpleDateFormat(myFormat);
        textViewReminder.setText(sdf.format(myCalender.getTime()));
    }
    DatePickerDialog.OnDateSetListener date= new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth)
        {
            myCalender.set(Calendar.YEAR,year);
            myCalender.set(Calendar.MONTH,monthOfYear);
            myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabe();
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.setReminder:
                new DatePickerDialog(homeScreenActivity, date, myCalender.get(Calendar.YEAR)
                         ,myCalender.get(Calendar.MONTH),
                          myCalender.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            case R.id.ic_action_color_pick:
                getColorPicker();
            default:
               return super.onOptionsItemSelected(item);
        }
    }
    private void getColorPicker()
    {
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowPresets(true)
                .setDialogId(DIALOG_ID)
                .setColor(Color.BLACK)
                .setShowAlphaSlider(true)
                .show(getActivity());
    }
    public void setColor(int color)
    {
        newcolor= String.valueOf(color);
        linearlayout.setBackgroundColor(color);
    }
}
