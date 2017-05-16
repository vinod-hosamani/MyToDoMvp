package com.app.todo.homescreen.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.todo.R;
import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.HomeScreenPresenter;
import com.app.todo.homescreen.presenter.TodoNotesPresenter;
import com.app.todo.homescreen.presenter.TodoNotesPresenterInterface;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.app.todo.session.SessionManagement;
import com.app.todo.util.SwipeAction;

import java.util.ArrayList;
import java.util.List;

public class TodoNotesFragment extends Fragment implements TodoNotesFragmentInterface{

    HomeScreenActivity homeScreenActivity;
    TodoNotesPresenterInterface presenter;
    SessionManagement session;
    List<TodoItemModel> allData;
    TodoItemAdapter todoItemAdapter;
    boolean isList;
    RecyclerView toDoItemRecycler;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    SwipeAction swipeAction;
    ItemTouchHelper itemTouchHelper;

    public TodoNotesFragment(HomeScreenActivity homeScreenActivity) {
        this.homeScreenActivity = homeScreenActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_home_screen, container, false);
        initView(view);

        presenter.getTodoNoteFromServer(session.getUserDetails().getId());

        allData = todoItemAdapter.getAllDataList();
        return view;
    }

    private void initView(View view) {
        /*check whether listview or gridview*/
        isList = true;

        toDoItemRecycler = (RecyclerView) view.findViewById(R.id.recycler_todo_item);
        todoItemAdapter = new TodoItemAdapter(homeScreenActivity, this);

        session = new SessionManagement(homeScreenActivity);
        presenter = new TodoNotesPresenter(homeScreenActivity, this);

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1
                , StaggeredGridLayoutManager.VERTICAL);
        toDoItemRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        toDoItemRecycler.setAdapter(todoItemAdapter);
        swipeAction = new SwipeAction(0, SwipeAction.left | SwipeAction.right, todoItemAdapter
                , homeScreenActivity);
        itemTouchHelper = new ItemTouchHelper(swipeAction);
        itemTouchHelper.attachToRecyclerView(toDoItemRecycler);

    }

    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList) {
        List<TodoItemModel> nonArchievedList = new ArrayList<>();

        for (TodoItemModel model :
                noteList) {
            if (!model.isArchieved()) {
                nonArchievedList.add(model);
            }
        }
        todoItemAdapter.setTodoList(nonArchievedList);
    }

    @Override
    public void getNoteFailure(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;

    @Override
    public void showDialog(String message) {
        if (!homeScreenActivity.isFinishing()) {
            progressDialog = new ProgressDialog(homeScreenActivity);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (!homeScreenActivity.isFinishing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void deleteTodoModelFailure(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteTodoModelSuccess(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchieveFailure(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchieveSuccess(String message) {
        Toast.makeText(homeScreenActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {

        searchText = searchText.toLowerCase();

        List<TodoItemModel> noteList = new ArrayList<>();

        for (TodoItemModel model :
                allData) {
            if(model.getTitle().toLowerCase().contains(searchText)){
                noteList.add(model);
            }
        }

        todoItemAdapter.setFilter(noteList);
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(int pos) {
        homeScreenActivity.addTodoFab.setVisibility(View.INVISIBLE);
        TodoItemModel model = todoItemAdapter.getItemModel(pos);

        Bundle args = new Bundle();
        args.putString(Constant.key_title, model.getTitle());
        args.putString(Constant.key_note, model.getNote());
        args.putString(Constant.key_reminder, model.getReminderDate());
        args.putInt(Constant.key_note_id, model.getNoteId());
        args.putString(Constant.key_startDate, model.getStartDate());

        AddTodoFragment fragment = new AddTodoFragment(homeScreenActivity);
        AddTodoFragment.add = false;
        AddTodoFragment.edit_pos = pos;
        fragment.setArguments(args);
        homeScreenActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.todo_item_fragment, fragment, "editTodo")
                .addToBackStack(null)
                .commit();

        todoItemAdapter.notifyDataSetChanged();
    }
}