package com.dinkapach.android.todolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Din on 12/10/2017.
 */

public class TaskContentProvider extends ContentProvider {

    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;
    private TodoListDbHelper mTodoListDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //whole table
        uriMatcher.addURI(TodoListContract.AUTHORITY, TodoListContract.PATH_TASKS, TASKS);
        //single item
        uriMatcher.addURI(TodoListContract.AUTHORITY, TodoListContract.PATH_TASKS + "/#", TASK_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mTodoListDbHelper = new TodoListDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match){
            case TASKS:
                returnCursor = getAllTasks(columns, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    private Cursor getAllTasks(@Nullable String[] columns, @Nullable String selection,
                               @Nullable String[] selectionArgs, @Nullable String sortOrder){
        final SQLiteDatabase db = mTodoListDbHelper.getReadableDatabase();
        return db.query(
                TodoListContract.TaskEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case TASKS:
                returnUri = insertTaskIntoDb(uri, contentValues);
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    private Uri insertTaskIntoDb(Uri uri, ContentValues contentValues){
        final SQLiteDatabase db = mTodoListDbHelper.getWritableDatabase();
        long id = db.insert(
                TodoListContract.TaskEntry.TABLE_NAME,
                null,
                contentValues);
        if (id > 0){
            return ContentUris.withAppendedId(TodoListContract.TaskEntry.CONTENT_URI, id);
        }
        else{
            throw new SQLException("faild to insert row into " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int deletedTasksCount;
        switch (match){
            case TASK_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deletedTasksCount = deleteTaskWithId(id);
                break;
            case TASKS:
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if (deletedTasksCount > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedTasksCount;
    }

    private int deleteTaskWithId(String id){
        final SQLiteDatabase db = mTodoListDbHelper.getWritableDatabase();
        return db.delete(TodoListContract.TaskEntry.TABLE_NAME,
                "_id=?",
                new String[]{id});
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
