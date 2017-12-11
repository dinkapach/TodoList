package com.dinkapach.android.todolist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dinkapach.android.todolist.data.TodoListContract;
import com.dinkapach.android.todolist.data.TodoListDbHelper;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private EditText mTaskTitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initViewComponents();

    }

    private void initViewComponents(){
        mTaskTitleEditText = findViewById(R.id.et_task_title);
    }

    public void addTask(View view){
        String taskTitle = mTaskTitleEditText.getText().toString();
        addNewTask(taskTitle);
        finish();
    }

    private void addNewTask(String title){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TaskEntry.COLUMN_NAME_TITLE, title);
        Uri uri = getContentResolver().insert(TodoListContract.TaskEntry.CONTENT_URI, contentValues);
        Log.d(TAG, "added new task: " + uri);
    }
}
