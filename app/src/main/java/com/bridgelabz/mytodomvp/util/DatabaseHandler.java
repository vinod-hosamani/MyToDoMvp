package com.bridgelabz.mytodomvp.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bridgelabz.mytodomvp.homescreen.model.TodoItemModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String db_name="db_todo";
    public static final int db_version = 1;
    public static final String tbl_name = "tbl_todo";
    public static final String key_title = "title";
    public static final String key_note = "note";

    private static DatabaseHandler dbh;

    private DatabaseHandler(Context context)
    {
        super(context, db_name, null, db_version);
    }

    public static DatabaseHandler getInstance(Context context){
        if(dbh == null){
            dbh = new DatabaseHandler(context);
        }
        return dbh;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_todo_tbl = "create table "+tbl_name+"(" +
                ""+key_title+" int primary key, "+key_note+" text)";

        db.execSQL(create_todo_tbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String drop_todo_tbl = "drop table if exist" +tbl_name;
        db.execSQL(drop_todo_tbl);

        onCreate(db);
    }

    public void addTodo(TodoItemModel model){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(key_title, model.getTitle());
        content.put(key_note, model.getNote());

        db.insert(tbl_name, null, content);

        db.close();
    }

    public TodoItemModel getTodo(String title)
    {
        String select_todo = "select * from "+tbl_name+" where "+key_title+" = '"+title+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select_todo, null);

        if(cursor !=null)
            cursor.moveToFirst();

        TodoItemModel model = new TodoItemModel();
        model.setTitle(cursor.getString(0));
        model.setNote(cursor.getString(1));
        db.close();
        return model;
    }

    public List<TodoItemModel> getAllTodo(){
        List<TodoItemModel> todoList = new ArrayList<>();

        String select_all_todo = "select * from "+tbl_name;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select_all_todo, null);

        if(cursor.moveToFirst()){
            do {
                TodoItemModel model = new TodoItemModel();
                model.setTitle(cursor.getString(0));
                model.setNote(cursor.getString(1));
                todoList.add(model);
            }while (cursor.moveToNext());
        }
        return todoList;
    }

    public int updateTodo(TodoItemModel model){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(key_title, model.getTitle());
        values.put(key_note, model.getNote());

        return db.update(tbl_name, values, key_title+"=?", new String[]{model.getTitle()});
    }

    public int deleteTodo(TodoItemModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(tbl_name, key_title+"=?", new String[]{model.getTitle()});
        db.close();
        return i;
    }

    public int getTodoCount() {
        String countQuery = "SELECT  * FROM " + tbl_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
}