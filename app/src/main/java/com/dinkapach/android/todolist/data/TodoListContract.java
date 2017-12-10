package com.dinkapach.android.todolist.data;

import android.provider.BaseColumns;

/**
 * Created by Din on 12/10/2017.
 */

public final class TodoListContract {

    private TodoListContract(){}

    public static class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TITLE = "title";
//        public static final String COLUMN_NAME_DUE_DATE = "due_date";
    }
}
