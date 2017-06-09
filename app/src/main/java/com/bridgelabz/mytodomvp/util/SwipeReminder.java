package com.bridgelabz.mytodomvp.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.ReminderFragment;
import com.bridgelabz.mytodomvp.session.SessionManagement;

/**
 * Created by bridgeit on 6/6/17.
 */
public class SwipeReminder extends ItemTouchHelper.SimpleCallback
{
    public static final int left = ItemTouchHelper.LEFT;
    public static  final int right=ItemTouchHelper.RIGHT;
    public static final int up=ItemTouchHelper.UP;
    public static final int down=ItemTouchHelper.DOWN;

    TodoItemAdapter todoAdapter;
    ReminderFragment activity;
    Activity contex;
    SessionManagement session;

    public SwipeReminder(int dragDirs, int swipeDirs, TodoItemAdapter adapter, ReminderFragment activity, Activity context)
    {
        super(dragDirs, swipeDirs);
        todoAdapter=adapter;
        this.activity=activity;
        this.contex=context;
        session=new SessionManagement(activity);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
                moveToTrash(position);
                break;
            case right:
                //goToToNotes(position);
                break;
        }
    }
    private void moveToTrash(int pos)
    {
        final TodoItemModel itemModel=todoAdapter.getItemModel(pos);
        activity.presenter.moveToTrash(itemModel);

        Snackbar snackbar=Snackbar.make(contex.getCurrentFocus(),"note is trashed",Snackbar.LENGTH_LONG).setAction("unco",
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //moveToAgainNotesFromTrash(itemModel);
                        Snackbar s=Snackbar.make(contex.getCurrentFocus(),"undodone",Snackbar.LENGTH_SHORT);
                        s.show();
                    }
                });
        snackbar.show();
    }
}
