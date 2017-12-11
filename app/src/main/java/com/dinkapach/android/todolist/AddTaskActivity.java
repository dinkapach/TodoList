package com.dinkapach.android.todolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dinkapach.android.todolist.data.TodoListContract;
import com.dinkapach.android.todolist.data.TodoListDbHelper;

public class AddTaskActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 2;
    private EditText mTaskTitleEditText;
    private Uri mUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initViewComponents();
        setupEditMode();
    }

    private void setupEditMode(){
        mUri = getIntent().getData();
        if (isEditMode()){
            getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        }
    }

    private boolean isEditMode(){
        return mUri != null;
    }

    private void initViewComponents(){
        mTaskTitleEditText = findViewById(R.id.et_task_title);
    }

    private void addTask(){
        String taskTitle = mTaskTitleEditText.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TaskEntry.COLUMN_NAME_TITLE, taskTitle);
        if (isEditMode()){
            getContentResolver().update(
                    mUri,
                    contentValues,
                    TodoListContract.TaskEntry._ID + "=" + mUri.getLastPathSegment(),
                    null);
        } else {
            Uri uri = getContentResolver().insert(TodoListContract.TaskEntry.CONTENT_URI, contentValues);
        }
//        Log.d(TAG, "added new task: " + uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case TASK_LOADER_ID:
                return new CursorLoader(this, mUri, null, null,
                        null, null);
            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        boolean cursorHasValidData = cursor != null && cursor.moveToFirst();
        if (!cursorHasValidData){
            return;
        }

        String taskTitle =
                cursor.getString(cursor.getColumnIndex(TodoListContract.TaskEntry.COLUMN_NAME_TITLE));
        mTaskTitleEditText.setText(taskTitle);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onClickButtonAdd(View view) {
        addTask();
        finish();
    }
}
