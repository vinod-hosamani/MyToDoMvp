package com.bridgelabz.mytodomvp.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.ArchiveFragment;
import com.bridgelabz.mytodomvp.session.SessionManagement;

/**
 * Created by bridgeit on 31/5/17.
 */
public class SwipeArchive  extends ItemTouchHelper.SimpleCallback
{
    public static final int left = ItemTouchHelper.LEFT;
    public static  final int right=ItemTouchHelper.RIGHT;
    public static final int up=ItemTouchHelper.UP;
    public static final int down=ItemTouchHelper.DOWN;

    TodoItemAdapter todoAdapter;
    ArchiveFragment activity;
    Activity contex;
    SessionManagement session;

    public SwipeArchive(int dragDirs, int swipeDirs,TodoItemAdapter adapter,ArchiveFragment activity,Activity context)
    {
        super(dragDirs, swipeDirs);
        todoAdapter=adapter;
        this.activity=activity;
        this.contex=context;
        session=new SessionManagement(activity);
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target)
    {
        todoAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        final int position=viewHolder.getAdapterPosition();
        switch (direction)
        {
            case left:
                 goToTrash(position);
                 break;
            case right:
                 goToToNotes(position);
                 break;
        }
    }
    private  void notesAgainFromTrash(TodoItemModel itemModel)
    {
        activity.presenter.notesAgainFromTrash(itemModel);
    }
    private  void notesAgainFromNotes(TodoItemModel itemModel)
    {
        activity.presenter.notesAgainFromNotes(itemModel);
    }

    public void goToTrash(int pos)
    {
        final TodoItemModel itemModel=  todoAdapter.getItemModel(pos);
        activity.presenter.goToTrash(itemModel);

        Snackbar snackbar=Snackbar.make(contex.getCurrentFocus(),"note is arvhieved",Snackbar.LENGTH_LONG).setAction("unod",
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //moveToAgainNotes(itemModel);
                        notesAgainFromTrash(itemModel);
                        Snackbar s=Snackbar.make(contex.getCurrentFocus(),"undodone",Snackbar.LENGTH_SHORT);
                        s.show();
                    }
                });
        snackbar.show();
    }
    public void goToToNotes(int pos)
    {
        final TodoItemModel itemModel=todoAdapter.getItemModel(pos);
        activity.presenter.goToNotes(itemModel);

        Snackbar snackbar=Snackbar.make(contex.getCurrentFocus(),"note is arvhieved",Snackbar.LENGTH_LONG).setAction("unod",
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //moveToAgainNotes(itemModel);
                        notesAgainFromNotes(itemModel);
                        Snackbar s=Snackbar.make(contex.getCurrentFocus(),"undodone",Snackbar.LENGTH_SHORT);
                        s.show();
                    }
                });
        snackbar.show();
    }
}

