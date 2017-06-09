package com.bridgelabz.mytodomvp.homescreen.ui.fragment;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bridgelabz.mytodomvp.R;
import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.presenter.ArchivePresenter;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.bridgelabz.mytodomvp.util.SwipeArchive;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 11/5/17.
 */
public class ArchiveFragment extends Fragment implements ArchiveFragmentInterface, SearchView.OnQueryTextListener {

    public static final String TAG ="ArchiveFragment";
    ArchiveFragment archiveFragment;
    private final Context mcontext;
    List<TodoItemModel> allData;
    RecyclerView archiveRecyclerView;
    TodoItemAdapter totoItemAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    HomeScreenActivity homeScreenActivity;
    public ArchivePresenter presenter;
    List<TodoItemModel> archiovedItemModels=new ArrayList<>();
    Menu menu;
    ItemTouchHelper itemTouchHelper;
    private SwipeArchive swipeArchive;

    @Override
    public void onResume()
    {
        super.onResume();
        ((HomeScreenActivity)getActivity()).setTitle("Archive");
    }
    public ArchiveFragment(Context context)
    {
        this.mcontext=context;
        presenter=new ArchivePresenter(mcontext,this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_archived_list,container,false);
        initView( view);
        setHasOptionsMenu(true);
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        presenter.getNoteList(userId);
        return view;
    }
    public void initView(View view)
    {
        archiveRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_archvied_list);
        setHasOptionsMenu(true);
        totoItemAdapter=new TodoItemAdapter(mcontext,this);
        archiveRecyclerView.setAdapter(totoItemAdapter);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,staggeredGridLayoutManager.VERTICAL);
        archiveRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        swipeArchive=new SwipeArchive(SwipeArchive.down|SwipeArchive.up|SwipeArchive.left|SwipeArchive.right,SwipeArchive.left | SwipeArchive.right,totoItemAdapter,ArchiveFragment.this,getActivity());
        itemTouchHelper=new ItemTouchHelper(swipeArchive);
        itemTouchHelper.attachToRecyclerView(archiveRecyclerView);
    }
    @Override
    public void getNoteListSuccess(List<TodoItemModel> noteList)
    {
        List<TodoItemModel> archiveList=new ArrayList<>();
        for(TodoItemModel model:noteList)
        {
            if(model.isArchieved())
                if(!model.isDeleted())
                {
                    archiveList.add(model);
                }
        }
        allData=archiveList;
        archiovedItemModels=archiveList;
        totoItemAdapter.setTodoList(archiveList);
    }
    @Override
    public void getNoteListFailure(String message)
    {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;
    @Override
    public void showProgressDialougeu(String message)
    {
        progressDialog=new ProgressDialog(mcontext);
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    @Override
    public void hideProgressDialogue()
    {
        progressDialog.dismiss();
    }
    @Override
    public void goToTrashSuccess(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void goToTrashFailure(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void goToNotesSuccess(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void goToNotesFailure(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void notesAgainFromTrashSuccess(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void notesAgainFromTrashFailure(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void notesAgainFromNotesSuccess(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void notesAgainFromNotesFailure(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onLongClick( final  TodoItemModel itemModel)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(homeScreenActivity);
        builder.setTitle(homeScreenActivity.getString(R.string.moingToNotes));
        builder.setMessage(homeScreenActivity.getString(R.string.askMoveToNoteMessage));
        builder.setPositiveButton(homeScreenActivity.getString(R.string.okButton),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
               homeScreenActivity.presenter.moveToNotes(itemModel);
            }
        });
        builder.setNegativeButton(homeScreenActivity.getString(R.string.cancleButton),
                new DialogInterface.OnClickListener()
                {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                   Toast.makeText(homeScreenActivity,homeScreenActivity.getString(R.string.cancel_message),
                   Toast.LENGTH_SHORT).show();
            }
        });

     builder.show();
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.archivemenu,menu);


        SearchManager searchManager=(SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.action_toggle)
        {
            Log.d("menu select","onOptionsItemSelected: toggle");
            toggle(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isList=true;

    private void toggle(MenuItem item)
    {

        if(isList)
        {
            staggeredGridLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.list_view);
            item.setTitle("show as list");
            isList=false;
        }
        else
        {
            staggeredGridLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.grid_view);
            item.setTitle("isGrid");
            isList=true;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {
        searchText=searchText.toLowerCase();
        List<TodoItemModel> noteList=new ArrayList<>();
        for(TodoItemModel model: allData)
        {
                if (model.getTitle().toLowerCase().contains(searchText)) {
                    noteList.add(model);
                }

        }
        totoItemAdapter.setFilter(noteList);
        return  true;
    }
}
