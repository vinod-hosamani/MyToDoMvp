package com.bridgelabz.mytodomvp.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.fragment.TrashFragment;
import com.bridgelabz.mytodomvp.session.SessionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 3/6/17.
 */
public class SwipeTrash extends ItemTouchHelper.SimpleCallback
{

    public static final int left = ItemTouchHelper.LEFT;
    public static  final int right=ItemTouchHelper.RIGHT;
    public static final int up=ItemTouchHelper.UP;
    public static final int down=ItemTouchHelper.DOWN;

    TodoItemAdapter todoAdapter;
    TrashFragment activity;
    Activity contex;
    SessionManagement session;

    public SwipeTrash(int dragDirs, int swipeDirs,TodoItemAdapter adapter,TrashFragment activity,Activity context)
    {
        super(dragDirs, swipeDirs);
        todoAdapter=adapter;
        this.activity=activity;
        this.contex=context;
        session=new SessionManagement(activity);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
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
                deletePermanent(position);
                break;
            case right:
                moveToArchive(position);
                break;
        }
    }
    public void deletePermanent(int pos)
    {
        TodoItemModel itemModel=todoAdapter.getItemModel(pos);
        List<TodoItemModel> list=todoAdapter.getAllDataList();
        List<TodoItemModel> tempList=new ArrayList<>();
        for (TodoItemModel model : list)
        {
            if (model.getStartDate().equals(itemModel.getStartDate()) &&
                    model.getNoteId() > itemModel.getNoteId())
            {
                model.setNoteId(model.getNoteId()-1);
                tempList.add(model);
            }
        }
        activity.presenter.permanentDelete(tempList,itemModel,pos);
        todoAdapter.removeItem(pos);
    }

    public void moveToArchive(int pos)
    {
        final TodoItemModel itemModel=  todoAdapter.getItemModel(pos);
        activity.presenter.moveToArchiveFromTrash(itemModel);

        Snackbar snackbar=Snackbar.make(contex.getCurrentFocus(),"note is arvhieved",Snackbar.LENGTH_LONG).setAction("unod",
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        //moveToAgainNotes(itemModel);

                        Snackbar s=Snackbar.make(contex.getCurrentFocus(),"undodone",Snackbar.LENGTH_SHORT);
                        s.show();
                    }
                });
       // snackbar.show();
         }
}
