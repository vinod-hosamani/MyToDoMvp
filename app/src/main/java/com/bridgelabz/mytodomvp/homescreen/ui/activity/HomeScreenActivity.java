package com.bridgelabz.mytodomvp.homescreen.ui.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
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
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.ArchiveFragment;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.ReminderFragment;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.TodoNotesFragment;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.TrashFragment;
import com.bridgelabz.mytodomvp.session.SessionManagement;
import com.bridgelabz.mytodomvp.util.SwipeAction;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.bridgelabz.mytodomvp.homescreen.ui.fragment.TodoNotesFragment;

//import com.bridgelabz.mytodomvp.homescreen.ui.fragment.AddToDoFragment;


/**
 * Created by bridgeit on 8/5/17.
 */
public class HomeScreenActivity extends BaseActivity implements HomeScreenActivityInterface,ColorPickerDialogListener
{
    private static final int result_load_img=1;
    public FloatingActionButton addTodoFab;
    public TodoItemAdapter todoItemAdapter;
    public HomeScreenPresenter presenter;
    StaggeredGridLayoutManager mstaggeredGridLayoutManager;
    SessionManagement session;
    AddToDoFragment addToDoFragment;
    public boolean isList;
 //   RecyclerView toDoItemRecycler;
    public Menu menu;
   // public swipeAction;
    SwipeAction swipeAction;
    ItemTouchHelper itemTouchHelper;
    ArchiveFragment archievedFragment;
   // ArchiveFragment archiveFragment;
    ProgressDialog progressDialog;
    /*Drawer layout variables*/
    AppCompatTextView txtUsername;
    AppCompatTextView txtUserEmail;
    CircleImageView imageViewUserProfile;
    /*Fb login*/
    String fbProfileUrl;

    String currentUserId;
    //ArchiveFragment archiveFragment;
    ReminderFragment reminderFragment;
    TodoNotesFragment todoNotesFragment;
    TrashFragment trashFragment;

    List<TodoItemModel> allData;
    public GoogleSignInOptions googleSignInOptions;
    public GoogleApiClient googleApiClient;

    DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initView();
        presenter.getTodoNoteFromServer(session.getUserDetails().getId());
        allData=todoItemAdapter.getAllDataList();
         /*ddrawer part*/
        setTitle(Constant.note_title);
        addTodoFab.setVisibility(View.VISIBLE);
        addToDoFragment();

        if(session.isFbLoggedIn())
        {
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
            Uri profileUrl = session.getProfilUrl();

            if(!profileUrl.toString().equals(""))
            {
                Glide.with(this).load(profileUrl).into(imageViewUserProfile);
            }
            else
            {
                imageViewUserProfile.setImageResource(R.drawable.images);
            }
            imageViewUserProfile.setOnClickListener(this);
        }
        //allData=todoItemAdapter.getAll
    }
    @Override
    public void initView()
    {
        setTitle(Constant.note_title);
        addTodoFab=(FloatingActionButton)findViewById(R.id.fab_add_todo);
        addTodoFab.setOnClickListener(this);
        addTodoFab.setVisibility(View.VISIBLE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        isList=true;

        //toDoItemRecycler=(RecyclerView)findViewById(R.id.recycler_todo_Item);
        todoItemAdapter=new TodoItemAdapter(this,this);
        session=new SessionManagement(this);
        presenter=new HomeScreenPresenter(this,this);

        mstaggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
       // toDoItemRecycler.setLayoutManager(mstaggeredGridLayoutManager);
        //toDoItemRecycler.setAdapter(todoItemAdapter);
        swipeAction=new SwipeAction(0,SwipeAction.left |SwipeAction.right,todoItemAdapter,this);
        itemTouchHelper=new ItemTouchHelper(swipeAction);
       // itemTouchHelper.attachToRecyclerView(toDoItemRecycler);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,
                toolbar,R.string.navigatinOpne,R.string.navigationClose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        txtUsername=(AppCompatTextView)header.findViewById(R.id.textViewUsername);
        txtUserEmail=(AppCompatTextView)header.findViewById(R.id.textViewUserEmail);
        imageViewUserProfile=(CircleImageView)header.findViewById(R.id.imageViewUserProfile);

        currentUserId=session.getUserDetails().getId();
    }
    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
            addTodoFab.setVisibility(View.VISIBLE);
        }
        else if(getSupportFragmentManager().getBackStackEntryCount()==1)
        {
            super.onBackPressed();
            finish();
        }
        else
        {
            getSupportFragmentManager().popBackStack();
        }
        //setTitle(Constant.note_title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout)
        {
            showProgressDialogue("loading....");
            if (session.isGoogleLoggedIn())
            {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>()
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
            hideProgressDialogu();
            finish();
            return true;
        }

        if (id == R.id.action_toggle)
        {
            toggle();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void getNoteSuccess(List<TodoItemModel> noteList)
    {
        List<TodoItemModel> nonTrashList=new ArrayList<>();
        for(TodoItemModel model:noteList)
        {
            if(!model.isArchieved())
            {
                if(!model.isDeleted())
                {
                    nonTrashList.add(model);

                }
            }
        }
       todoItemAdapter.setTodoList(nonTrashList);
    }

    @Override
    public void getNoteFailure(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialogue(String message)
    {
      if(!isFinishing())
      {
          progressDialog=new ProgressDialog(this);
          progressDialog.setMessage(message);
          progressDialog.show();
      }
    }

    @Override
    public void hideProgressDialogu()
    {
    if(!isFinishing() && progressDialog!=null)
    {
        progressDialog.dismiss();
    }
    }

    @Override
    public void deleteTodoModelFailure(String message)
    {
      Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteTodoModelSuccess(String message)
    {
      Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToArchiveFailure(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void moveToArchiveSuccess(String message)
    {
     Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToTrashFailure(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToTrashSuccess(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToReminderSuccess(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToReminderFailure(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadSuccess(Uri downloadUrl)
    {
        Glide.with(this).load(downloadUrl).into(imageViewUserProfile);
        session.setProfilePic(downloadUrl);
    }

    @Override
    public void uploadFailure(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void toggle()
    {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isList)
        {
            mstaggeredGridLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.list_view);
            item.setTitle("Show as list");
            isList = false;
        }
        else
        {
            mstaggeredGridLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.grid_view);
            item.setTitle("Show as grid");
            isList = true;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
  public boolean onNavigationItemSelected(MenuItem item)
  {
      // Handle navigation view item clicks here.
      int id = item.getItemId();

      switch (item.getItemId())
      {
          case R.id.nav_notes :
               addToDoFragment();
               break;
          case R.id.nav_archieved :
               addArchive();
               break;
          case R.id.nav_reminder :
               addReminder();
               break;
          case R.id.nav_trash :
               addTrash();
               break;
      }
      drawer.closeDrawer(GravityCompat.START);
      return true;
  }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.fab_add_todo:
                 addTodoFab.setVisibility(View.INVISIBLE);
                 addTodoTask();
                 break;
            case R.id.imageViewUserProfile:
                 Intent galleryIntent = new Intent(Intent.ACTION_PICK
                        ,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(galleryIntent, result_load_img);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if (requestCode == result_load_img && resultCode == RESULT_OK && data != null)
            {
                Uri selectedImage = data.getData();
                Glide.with(this).load(selectedImage).into(imageViewUserProfile);
                CropImage.activity(selectedImage).start(this);

            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode ==  RESULT_OK)
                {
                    Uri cropedImage = result.getUri();
                    presenter.uploadProfilePic(currentUserId, cropedImage);
                }
                else
                {
                    Toast.makeText(this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, R.string.image_pick_error, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        AddToDoFragment.add=false;
        AddToDoFragment.editposition=pos;
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer,fragment,"editTOdo")
                .addToBackStack(null)
                .commit();
        setTitle(Constant.update_fragment_title);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, fragment, "editTodo")
                .addToBackStack(null)
                .commit();
        todoItemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
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
        todoItemAdapter.setFilter(noteList);
        return true;
    }
    public void updateAdapter(int pos, TodoItemModel model)
    {
        todoItemAdapter.updateItem(pos, model);
    }

  public void addTodoTask()
  {
      addToDoFragment =new AddToDoFragment(this);
      getSupportFragmentManager().beginTransaction()
              .replace(R.id.frameContainer,addToDoFragment, "todoList")
              .addToBackStack(null)
              .commit();
  }

    public void addToDoFragment()
     {
       setTitle(Constant.note_title);
       addTodoFab.setVisibility(View.VISIBLE);
         if(todoNotesFragment == null)
        todoNotesFragment = new TodoNotesFragment(this);

       getSupportFragmentManager().beginTransaction()
            .replace(R.id.frameContainer,todoNotesFragment, TodoNotesFragment.TAG)
            .addToBackStack(null)
            .commit();
     }
    public void addArchive()
    {
        setTitle(Constant.archieve_title);
        addTodoFab.setVisibility(View.INVISIBLE);
        if(archievedFragment == null)
            archievedFragment = new ArchiveFragment(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer,archievedFragment, "archievedList")
                .addToBackStack(null)
                .commit();
    }
    public void addReminder()
    {
        setTitle(Constant.reminder_title);
        addTodoFab.setVisibility(View.INVISIBLE);
        if(reminderFragment==null)
            reminderFragment = new ReminderFragment(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, reminderFragment, "Reminder")
                .addToBackStack(null)
                .commit();
    }

    public  void addTrash()
    {
        setTitle(Constant.trash_title);
        addTodoFab.setVisibility(View.INVISIBLE);
        if(trashFragment==null)
            trashFragment = new TrashFragment(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, trashFragment,"Trash")
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onColorSelected(int dialogId, @ColorInt int color)
    {
        if(addToDoFragment!=null)
        {
            addToDoFragment.setColor(color);
        }
        else
        {
            todoNotesFragment.setColor(color);
        }
    }
    @Override
    public void onDialogDismissed(int dialogId)
    {

    }
}
