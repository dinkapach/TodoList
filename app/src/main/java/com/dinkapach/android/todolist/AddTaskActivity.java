package com.dinkapach.android.todolist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dinkapach.android.todolist.data.TodoListContract;
import com.dinkapach.android.todolist.data.TodoListDbHelper;

public class AddTaskActivity extends AppCompatActivity {

    private EditText mTaskTitleEditText;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initViewComponents();

        //create db helper
        TodoListDbHelper dbHelper = new TodoListDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    private void initViewComponents(){
        mTaskTitleEditText = findViewById(R.id.et_task_title);
    }

    public void addTask(View view){
        String taskTitle = mTaskTitleEditText.getText().toString();
        addNewTask(taskTitle);
        finish();
    }

    private long addNewTask(String title){
        ContentValues cv = new ContentValues();
        cv.put(TodoListContract.TaskEntry.COLUMN_NAME_TITLE, title);
        return mDb.insert(TodoListContract.TaskEntry.TABLE_NAME, null, cv);
    }
}
