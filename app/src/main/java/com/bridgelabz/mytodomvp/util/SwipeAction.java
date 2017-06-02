package com.bridgelabz.mytodomvp.util;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bridgelabz.mytodomvp.adapter.TodoItemAdapter;
import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;
import com.bridgelabz.mytodomvp.homescreen.ui.activity.HomeScreenActivity;
import com.bridgelabz.mytodomvp.session.SessionManagement;

/**
 * Created by bridgeit on 11/5/17.
 */
public class SwipeAction extends ItemTouchHelper.SimpleCallback
{
    public static final int left = ItemTouchHelper.LEFT;
    public static  final int right=ItemTouchHelper.RIGHT;

    TodoItemAdapter todoAdapter;
    HomeScreenActivity activity;
    //TodoNotesFragment activity;
    SessionManagement session;

    public SwipeAction(int dragDirs, int swipeDirs,TodoItemAdapter adapter,HomeScreenActivity activity)
    {
        super(dragDirs, swipeDirs);
        todoAdapter=adapter;
        this.activity=activity;
        session=new SessionManagement(activity);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
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
                 movoToArchieved(position);
                 break;
        }
    }


   /* private void deleteItem(int pos)
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
        activity.presenter.deleteTodoModel(tempList, itemModel ,pos);
        todoAdapter.removeItem(pos);

    }*/
    private void moveToAgainNotes(TodoItemModel itemModel)
    {
        activity.presenter.moveToNotes(itemModel);
    }

    private void moveToAgainNotesFromTrash(TodoItemModel itemModel)
    {
        activity.presenter.moveToNotesFromTrash(itemModel);
    }

    private void movoToArchieved(int pos)
    {
        final TodoItemModel itemModel=  todoAdapter.getItemModel(pos);
        activity.presenter.moveToArchive(itemModel);

        Snackbar snackbar=Snackbar.make(activity.getCurrentFocus(),"note is arvhieved",Snackbar.LENGTH_LONG).setAction("unod",
        new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                moveToAgainNotes(itemModel);
                Snackbar s=Snackbar.make(activity.getCurrentFocus(),"undodone",Snackbar.LENGTH_SHORT);
                s.show();
            }
        });
        snackbar.show();
    }

    private void moveToTrash(int pos)
    {
        final  TodoItemModel itemModel=todoAdapter.getItemModel(pos);
        activity.presenter.moveToTrash(itemModel);

        Snackbar snackbar=Snackbar.make(activity.getCurrentFocus(),"note is trashed",Snackbar.LENGTH_LONG).setAction("unco",
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        moveToAgainNotesFromTrash(itemModel);
                        Snackbar s=Snackbar.make(activity.getCurrentFocus(),"undodone",Snackbar.LENGTH_SHORT);
                        s.show();
                    }
                });
        snackbar.show();
    }
   /* private  void moveToReminder(int pos)
    {
        final TodoItemModel itemModel=todoAdapter.getItemModel(pos);
        activity.presenter.moveToReminder(itemModel);
    }*/

}
