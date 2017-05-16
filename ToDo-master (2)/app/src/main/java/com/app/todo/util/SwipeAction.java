package com.app.todo.util;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.app.todo.R;
import com.app.todo.adapter.TodoItemAdapter;
import com.app.todo.constants.Constant;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.homescreen.ui.activity.HomeScreenActivity;
import com.app.todo.homescreen.ui.fragment.AddTodoFragment;
import com.app.todo.homescreen.ui.fragment.TodoNotesFragment;
import com.app.todo.session.SessionManagement;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by bridgeit on 11/4/17.
 */

public class SwipeAction extends ItemTouchHelper.SimpleCallback {

    public static final int left = ItemTouchHelper.LEFT;
    public static final int right = ItemTouchHelper.RIGHT;
    private static final String TAG = "move";

    TodoItemAdapter todoAdapter;
    HomeScreenActivity activity;
    SessionManagement session;

    public SwipeAction(int dragDirs, int swipeDirs, TodoItemAdapter adapter
            , HomeScreenActivity activity) {
        super(dragDirs, swipeDirs);
        todoAdapter = adapter;
        this.activity = activity;
        session = new SessionManagement(activity);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder
            , RecyclerView.ViewHolder target) {
// Collections.swap(toDoItemModels,viewHolder.getAdapterPosition(), target.getAdapterPosition());
        Log.i(TAG, "onMove: " + viewHolder.getAdapterPosition() + " target" + target.getAdapterPosition());
        int from, destination;
        from = viewHolder.getAdapterPosition();
        destination = target.getAdapterPosition();
        todoAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
/*updateNotePresenter.getMoveNotes(mUserUID, toDoItemModels.get(from), toDoItemModels.get(destination));*/
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int pos = viewHolder.getAdapterPosition();
        switch (direction){
            case left:
                deleteItem(pos);
                break;

            case right:
                moveToArchieved(pos);
                break;
        }
    }

    private void moveToAgainNotes(TodoItemModel itemModel) {
        activity.presenter.moveToNotes(itemModel);
    }

    private void moveToArchieved(int pos) {

        final TodoItemModel itemModel = todoAdapter.getItemModel(pos);
        activity.presenter.moveToArchieve(itemModel);

        Snackbar snackbar = Snackbar.make(activity.getCurrentFocus()
                , "note has been Archieved", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        moveToAgainNotes(itemModel);

                        Snackbar snackbar1 = Snackbar.make(activity.getCurrentFocus()
                                , "Undo done", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                });
        snackbar.show();
    }

    private void deleteItem(int pos) {
        Log.v("pos",String.valueOf(pos));

        TodoItemModel itemModel = todoAdapter.getItemModel(pos);
        List<TodoItemModel> list = todoAdapter.getAllDataList();
        List<TodoItemModel> tempList = new ArrayList<>();
        for (TodoItemModel model : list){
            if (model.getStartDate().equals(itemModel.getStartDate()) &&
                    model.getNoteId() > itemModel.getNoteId()){
                model.setNoteId(model.getNoteId()-1);
                tempList.add(model);
            }
        }
        activity.presenter.deleteTodoModel(tempList, itemModel ,pos);
        todoAdapter.removeItem(pos);
    }
}