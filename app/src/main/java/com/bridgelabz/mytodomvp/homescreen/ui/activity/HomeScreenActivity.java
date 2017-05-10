package com.bridgelabz.mytodomvp.homescreen.ui.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.base.BaseActivity;
import com.bridgelabz.mytodomvp.constants.Constant;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.HomeScreenPresenter;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.AddToDoFragment;
import com.bridgelabz.mytodomvp.session.SessionManagement;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bridgeit on 8/5/17.
 */
public class HomeScreenActivity extends BaseActivity implements HomeScreenActivityInterface {

    private static final int result_load_img=1;
    public FloatingActionButton addTodoFab;
    public TodoItemAdapter todoItemAdapter;
    public HomeScreenPresenter presenter;
    StaggeredGridLayoutManager mstaggeredGridLayoutManager;
    SessionManagement session;
    public boolean isList;
    RecyclerView toDoItemRecycler;
    public Menu menu;
    //public swipeAction;
    ItemTouchHelper itemTouchHelper;
   // ArchievedFragment archievedFragment;
    ProgressDialog progressDialog;
    /*Drawer layout variables*/
    AppCompatTextView txtUsername;
    AppCompatTextView txtUserEmail;
    CircleImageView imageViewUserProfile;
    /*Fb login*/
    String fbProfileUrl;

    List<TodoItemModel> allData;
    public  GoogleSignInOptions googleSignInOptions;
    public  GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initView();
        initView();
        presenter.getTodoNoteFromServer(session.getUserDetails().getId());
        allData=todoItemAdapter.getAllDataList();

        if(session.isFbLoggedIn()){
            fbProfileUrl="https://graph.facebook.com/" + session.getUserDetails().getMobile() + "/picture?type=large";
            Glide.with(this).load(fbProfileUrl).into(imageViewUserProfile);
            txtUsername.setText(session.getUserDetails().getFullname());
            txtUserEmail.setText(session.getUserDetails().getEmail());

        }
        else if(session.isGoogleLoggedIn())
        {
            googleSignInOptions = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .build();
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                    .build();

            String photoURL = session.getUserDetails().getMobile();
            txtUsername.setText(session.getUserDetails().getFullname());
            txtUserEmail.setText(session.getUserDetails().getEmail());
            Glide.with(this).load(Uri.parse(photoURL)).into(imageViewUserProfile);
        }
        else
        {
            txtUsername.setText(session.getUserDetails().getFullname());
            txtUserEmail.setText(session.getUserDetails().getEmail());
            imageViewUserProfile.setOnClickListener(this);
        }


        //allData=todoItemAdapter.getAll
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            addTodoFab.setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();
            addTodoFab.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar,menu);
        this.menu=menu;

        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout)
        {
            //showprDialog(getString(R.string.loading))
            showProgressDialogue("loading....");
            if (session.isGoogleLoggedIn())
            {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>()
                        {
                            @Override
                            public void onResult(@NonNull Status status)
                            {
                                if (status.isSuccess())
                                {
                                    Toast.makeText(HomeScreenActivity.this,
                                            status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(HomeScreenActivity.this,
                                            status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }
            session.logoutUser();
           // hideDialog();
            hideProgressDialogu();
            finish();
            return true;
        }

        if (id == R.id.action_toggle) {
            toggle();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {

    }
   /* public void addTodoTask() {
        AddTodoFragment addTodoFragment = new AddTodoFragment(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.todo_item_fragment, addTodoFragment, "todoList")
                .addToBackStack(null)
                .commit();
    }*/

    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList) {

    }

    @Override
    public void getNoteFailure(String message) {

    }

    @Override
    public void showProgressDialogue(String message) {

    }

    @Override
    public void hideProgressDialogu() {

    }

    @Override
    public void deleteTodoModelFailure() {

    }

    @Override
    public void deleteTodoModelSuccess() {

    }

    @Override
    public void moveToArchiveFailure() {

    }

    @Override
    public void moveToArchiveSuccess() {

    }

    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isList)
        {
            mstaggeredGridLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");
            isList = false;
        }
        else
        {
            mstaggeredGridLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            isList = true;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_todo:
                addTodoFab.setVisibility(View.INVISIBLE);
/*
                addTodoTask();
*/
                break;

            case R.id.imageViewUserProfile:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, result_load_img);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(int pos)

    {

        addTodoFab.setVisibility(View.INVISIBLE);
        TodoItemModel model=todoItemAdapter.getItemModel(pos);

        Bundle arguments=new Bundle();
        arguments.putString(Constant.key_title,model.getTitle());
        arguments.putString(Constant.key_note,model.getNote());
        arguments.putString(Constant.key_reminder,model.getReminderDate());
        arguments.putString(Constant.key_note_id, String.valueOf(model.getNoteId()));
        arguments.putString(Constant.key_startDate,model.getStartDate());

        AddToDoFragment fragment=new AddToDoFragment(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText)
    {
        searchText=searchText.toLowerCase();
        List<TodoItemModel> noteList=new ArrayList<>();
        for(TodoItemModel model:allData)
        {
            if(model.getTitle().toLowerCase().contains(searchText))
            {
                noteList.add(model);
            }
        }
        todoItemAdapter.setFilter(noteList)
        return true;
    }
    public void updateAdapter(int pos, TodoItemModel model) {
        todoItemAdapter.updateItem(pos, model);
    }


}
