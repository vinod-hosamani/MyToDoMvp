package com.app.todo.homescreen.ui.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.todo.R;
import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.base.BaseActivity;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.presenter.HomeScreenPresenter;
import com.app.todo.homescreen.ui.fragment.AddTodoFragment;
import com.app.todo.homescreen.ui.fragment.ArchievedFragment;
import com.app.todo.homescreen.ui.fragment.ReminderFragment;
import com.app.todo.homescreen.ui.fragment.TodoNotesFragment;
import com.app.todo.session.SessionManagement;
import com.app.todo.util.SwipeAction;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.app.todo.login.ui.LoginActivity.googleApiClient;
import static com.app.todo.login.ui.LoginActivity.googleSignInOptions;

public class HomeScreenActivity extends BaseActivity
        implements HomeScreenActivityInterface {

    private static final int result_load_img = 1;
    public FloatingActionButton addTodoFab;
    public HomeScreenPresenter presenter;
    SessionManagement session;
    public boolean isList;
    public Menu menu;
    ProgressDialog progressDialog;
    /*Drawer layout variables*/
    AppCompatTextView txtUsername;

    AppCompatTextView txtUserEmail;
    CircleImageView imageViewUserProfile;
    /*Fb login*/
    String fbProfileUrl;
    String currentUserId;

    //Todo uncomment if idea is wrong
    /*SwipeAction swipeAction;
    ItemTouchHelper itemTouchHelper;
    public TodoItemAdapter todoItemAdapter;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    RecyclerView toDoItemRecycler;
    List<TodoItemModel> allData;*/

    ArchievedFragment archievedFragment;
    ReminderFragment reminderFragment;
    TodoNotesFragment todoNotesFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initView();

        //Todo uncomment if idea is wrong
        /*presenter.getTodoNoteFromServer(currentUserId);
        allData = todoItemAdapter.getAllDataList();*/

        //Todo comment if idea is wrong
        todoNotesFragment = new TodoNotesFragment(this);
        setTitle(Constant.note_title);
        addTodoFab.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.todo_item_fragment, todoNotesFragment, "todoNoteList")
                .addToBackStack(null)
                .commit();

        if (session.isFbLoggedIn()) {
            fbProfileUrl = "https://graph.facebook.com/" + session.getUserDetails().getMobile() +
                    "/picture?type=large";
            Glide.with(this).load(fbProfileUrl).into(imageViewUserProfile);
            txtUsername.setText(session.getUserDetails().getFullname());
            txtUserEmail.setText(session.getUserDetails().getEmail());
        } else if (session.isGoogleLoggedIn()) {
            googleSignInOptions = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();

            String photoURL = session.getUserDetails().getMobile();
            txtUsername.setText(session.getUserDetails().getFullname());
            txtUserEmail.setText(session.getUserDetails().getEmail());
            Glide.with(this).load(Uri.parse(photoURL)).into(imageViewUserProfile);
        } else {
            txtUsername.setText(session.getUserDetails().getFullname());
            txtUserEmail.setText(session.getUserDetails().getEmail());
            String profileUrl = session.getUserDetails().getImageUrl();

            if (!profileUrl.equals("")){
                Glide.with(this).load(profileUrl).into(imageViewUserProfile);
            }else {
                imageViewUserProfile.setImageResource(R.drawable.avatar);
            }

            imageViewUserProfile.setOnClickListener(this);
        }
    }
    @Override
    public void initView() {

        setTitle(Constant.note_title);

        addTodoFab = (FloatingActionButton) findViewById(R.id.fab_add_todo);
        addTodoFab.setOnClickListener(this);
        addTodoFab.setVisibility(View.VISIBLE);
        /*check whether listview or gridview*/
        isList = true;

        session = new SessionManagement(this);
        presenter = new HomeScreenPresenter(this, this);

        //Todo uncomment if idea is wrong
        /*toDoItemRecycler = (RecyclerView) findViewById(R.id.recycler_todo_item);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        toDoItemRecycler.setLayoutManager(mStaggeredGridLayoutManager);
        todoItemAdapter = new TodoItemAdapter(this, this);
        toDoItemRecycler.setAdapter(todoItemAdapter);
        swipeAction = new SwipeAction(0, SwipeAction.left | SwipeAction.right, todoItemAdapter, this);
        itemTouchHelper = new ItemTouchHelper(swipeAction);
        itemTouchHelper.attachToRecyclerView(toDoItemRecycler);*/

        /*Drawer layout*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        txtUsername = (AppCompatTextView) header.findViewById(R.id.textViewUsername);
        txtUserEmail = (AppCompatTextView) header.findViewById(R.id.textViewUserEmail);
        imageViewUserProfile = (CircleImageView) header.findViewById(R.id.imageViewUserProfile);

        currentUserId = session.getUserDetails().getId();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            addTodoFab.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
            addTodoFab.setVisibility(View.VISIBLE);
        }

        //Todo comment if idea is wrong
        setTitle(Constant.note_title);
        addTodoFab.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.todo_item_fragment, todoNotesFragment, "todoNoteList")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        this.menu = menu;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            showDialog(getString(R.string.loading));
            session.logoutUser();
            if (session.isGoogleLoggedIn()) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    Toast.makeText(HomeScreenActivity.this,
                                            status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(HomeScreenActivity.this,
                                            status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }
            hideDialog();
            finish();
            return true;
        }

        if (id == R.id.action_toggle) {
            //Todo uncomment if idea is wrong
            /*toggle();*/
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        archievedFragment = new ArchievedFragment(this);
        reminderFragment = new ReminderFragment(this);
        if (id == R.id.nav_notes) {

            setTitle(Constant.note_title);

            addTodoFab.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.todo_item_fragment, archievedFragment, "archievedList")
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_archieved) {

            setTitle(Constant.archieve_title);

            addTodoFab.setVisibility(View.INVISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.todo_item_fragment, archievedFragment, "archievedList")
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_reminder) {

            setTitle(Constant.reminder_title);

            addTodoFab.setVisibility(View.INVISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.todo_item_fragment, reminderFragment, "reminderList")
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Todo uncomment if idea is wrong
    /*private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isList) {
            mStaggeredGridLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");
            isList = false;
        } else {
            mStaggeredGridLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            isList = true;
        }
    }*/

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_add_todo:
                addTodoFab.setVisibility(View.INVISIBLE);
                addTodoTask();
                break;

            case R.id.imageViewUserProfile:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, result_load_img);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == result_load_img && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();

                CropImage.activity(selectedImage).start(this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode ==  RESULT_OK){
                    Uri cropedImage = result.getUri();
                    presenter.uploadProfilePic(currentUserId, cropedImage);
                }else {
                    Toast.makeText(this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, R.string.image_pick_error, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void addTodoTask() {
        setTitle(Constant.add_note_title);

        AddTodoFragment addTodoFragment = new AddTodoFragment(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.todo_item_fragment, addTodoFragment, "todoList")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClick(int pos) {

        //Todo uncomment if idea is wrong
        /*addTodoFab.setVisibility(View.INVISIBLE);
        TodoItemModel model = todoItemAdapter.getItemModel(pos);

        Bundle args = new Bundle();
        args.putString(Constant.key_title, model.getTitle());
        args.putString(Constant.key_note, model.getNote());
        args.putString(Constant.key_reminder, model.getReminderDate());
        args.putInt(Constant.key_note_id, model.getNoteId());
        args.putString(Constant.key_startDate, model.getStartDate());

        AddTodoFragment fragment = new AddTodoFragment(this);
        AddTodoFragment.add = false;
        AddTodoFragment.edit_pos = pos;
        fragment.setArguments(args);

        setTitle(Constant.update_fragment_title);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.todo_item_fragment, fragment, "editTodo")
                .addToBackStack(null)
                .commit();

        todoItemAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList) {
        /*List<TodoItemModel> nonArchievedList = new ArrayList<>();

        for (TodoItemModel model :
                noteList) {
            if (!model.isArchieved()) {
                nonArchievedList.add(model);
            }
        }
        todoItemAdapter.setTodoList(nonArchievedList);*/
    }

    @Override
    public void getNoteFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(String message) {
        if (!isFinishing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (!isFinishing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void deleteTodoModelFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteTodoModelSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchieveFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchieveSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadSuccess(Uri downloadUrl) {
        Glide.with(this).load(downloadUrl).into(imageViewUserProfile);
        session.setProfile(downloadUrl);
    }

    @Override
    public void uploadFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        //Todo uncomment if idea is wrong
     /*   searchText = searchText.toLowerCase();

        List<TodoItemModel> noteList = new ArrayList<>();

        for (TodoItemModel model :
                allData) {
            if(model.getTitle().toLowerCase().contains(searchText)){
                noteList.add(model);
            }
        }

        todoItemAdapter.setFilter(noteList);*/
        return true;
    }
}