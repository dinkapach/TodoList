package com.dinkapach.android.todolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Din on 12/10/2017.
 */

public class TodoListDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    public TodoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TODOLIST_TABLE =
                "CREATE TABLE " + TodoListContract.TaskEntry.TABLE_NAME + " (" +
                TodoListContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TodoListContract.TaskEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_TODOLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TodoListContract.TaskEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
