package com.dinkapach.android.todolist.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Din on 12/10/2017.
 */

public final class TodoListContract {

    public static final String AUTHORITY = "com.dinkapach.android.todolist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = TaskEntry.TABLE_NAME;

    private TodoListContract(){}

    public static class TaskEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_TITLE = "title";
        public static Uri buildUriWithId(long id){
            String stringId = Long.toString(id);
            return CONTENT_URI.buildUpon().appendPath(stringId).build();
        }
    }
}
