package com.dinkapach.android.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dinkapach.android.todolist.data.TodoListContract;
import com.dinkapach.android.todolist.data.TodoListDbHelper;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter mTaskAdapter;
    private SQLiteDatabase mDb;
    private RecyclerView mTaskListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskListRecyclerView = findViewById(R.id.rv_all_task_list);
        mTaskListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        TodoListDbHelper dbHelper = new TodoListDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllTasks();
        mTaskAdapter = new TaskAdapter(this, cursor);
        mTaskListRecyclerView.setAdapter(mTaskAdapter);
        createItemTouchHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCursor();
    }

    private void createItemTouchHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                if(removeTask(id)) {
                    refreshCursor();
                }
            }
        }).attachToRecyclerView(mTaskListRecyclerView);
    }

    private Cursor getAllTasks(){
        return mDb.query(
                TodoListContract.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private boolean removeTask(long id){
        return mDb.delete(
                TodoListContract.TaskEntry.TABLE_NAME,
                TodoListContract.TaskEntry._ID + "=" + id,
                null) > 0;
    }

    public void onClickButtonAdd(View view){
        startAddTaskActivity();
    }

    private void startAddTaskActivity(){
        Intent intentToStartAddTaskActivity = new Intent(this, AddTaskActivity.class);
        startActivity(intentToStartAddTaskActivity);
    }

    private void refreshCursor(){
        mTaskAdapter.swapCursor(getAllTasks());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_refresh:
//                mTaskAdapter.swapCursor(getAllTasks());
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
