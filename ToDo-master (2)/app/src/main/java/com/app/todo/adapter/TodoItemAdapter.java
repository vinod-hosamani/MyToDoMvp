package com.app.todo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.todo.R;
import com.app.todo.homescreen.model.TodoItemModel;
import com.app.todo.util.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class  TodoItemAdapter
        extends RecyclerView.Adapter<TodoItemAdapter.TaskViewHolder> {

    private static List<TodoItemModel> todoList;

    Context context;
    TodoItemModel model;
    OnNoteClickListener noteClickListener;
    DatabaseHandler database;
    OnLongClickListener onLongClickListener;

    public TodoItemAdapter(Context context, OnNoteClickListener noteClickListener){
        if(todoList == null) {
            database = DatabaseHandler.getInstance(context);
            this.todoList = new ArrayList<>();
        }
        this.noteClickListener = noteClickListener;
        this.context = context;
    }

    public TodoItemAdapter(Context context, OnLongClickListener onLongClickListener) {
        this.context = context;
        this.onLongClickListener = onLongClickListener;
    }

    public TodoItemAdapter(Context context){
        this.context = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_todo_item_list, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, final int position) {
        final TodoItemModel todoItemModel = todoList.get(position);
        holder.title.setText(todoItemModel.getTitle());
        holder.note.setText(todoItemModel.getNote());
        if(!todoItemModel.getReminderDate().equals("")){
            holder.reminderDate.setText(todoItemModel.getReminderDate());
        }else {
            holder.reminderDate.setText("");
        }

        if (noteClickListener != null) {
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (noteClickListener != null)
                                noteClickListener.onItemClick(position);
                        }
                    }
            );
        }

        if(onLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onLongClickListener.onLongClick(todoItemModel);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setTodoList(List<TodoItemModel> noteList){
        todoList.clear();
        notifyDataSetChanged();
        todoList.addAll(noteList);
        notifyDataSetChanged();
    }

    public void setFilter(List<TodoItemModel> noteList){
        todoList = new ArrayList<>();
        todoList.addAll(noteList);
        notifyDataSetChanged();
    }

    public void addItem(TodoItemModel model){
        todoList.add(model);
        notifyItemInserted(todoList.size());
    }

    public void removeItem(int pos){
        model = todoList.get(pos);
        /*database.deleteTodo(model);*/

        todoList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, todoList.size());
    }

    public void updateItem(int pos, TodoItemModel model){
        todoList.set(pos, model);
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{

        public AppCompatTextView title, note, reminderDate;
        public TaskViewHolder(View view) {
            super(view);
            title = (AppCompatTextView) view.findViewById(R.id.todo_title);
            note = (AppCompatTextView) view.findViewById(R.id.todo_note);
            reminderDate = (AppCompatTextView) view.findViewById(R.id.todo_reminder);
        }
    }

    public TodoItemModel getItemModel(int pos){
        return todoList.get(pos);
    }

    public List<TodoItemModel> getAllDataList() {
        return todoList;
    }

    public interface OnNoteClickListener{
        void onItemClick(int pos);
    }

    public interface OnLongClickListener{
        void onLongClick(TodoItemModel itemModel);
    }
}
