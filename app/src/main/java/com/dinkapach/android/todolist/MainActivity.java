package com.dinkapach.android.todolist;

import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import com.dinkapach.android.todolist.data.TodoListContract;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        TaskAdapter.TaskAdapterOnClickHandler{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASKS_LOADER_ID = 1;
    private TaskAdapter mTaskAdapter;
    private RecyclerView mTaskListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskListRecyclerView = findViewById(R.id.rv_all_task_list);
        mTaskListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTaskAdapter = new TaskAdapter(this, this);
        mTaskListRecyclerView.setAdapter(mTaskAdapter);
        createItemTouchHelper();
        getSupportLoaderManager().initLoader(TASKS_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartTaskLoader();
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
                if(deleteTask(id)) {
                    restartTaskLoader();
                }
            }
        }).attachToRecyclerView(mTaskListRecyclerView);
    }

    private boolean deleteTask(long id){
        return getContentResolver().delete(
                TodoListContract.TaskEntry.buildUriWithId(id),
                null,
                null) > 0;
    }

    public void onClickButtonAdd(View view){
        startAddTaskActivity();
    }

    private void startAddTaskActivity(){
        Intent intentToStartAddTaskActivity = new Intent(this, AddTaskActivity.class);
        startActivity(intentToStartAddTaskActivity);
    }

    private void restartTaskLoader(){
        Log.d(TAG, "restartTaskLoader");
        getSupportLoaderManager().restartLoader(TASKS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle loaderArgs) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if(mTaskData != null){
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }

            @Override
            public Cursor loadInBackground() {
                Log.d(TAG, "loadInBackground");
                try {
                    return getContentResolver().query(
                            TodoListContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                } catch (Exception e){
                    Log.e(TAG, "Failed to load tasks");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mTaskAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTaskAdapter.swapCursor(null);
    }

    //on click task item
    @Override
    public void onClick(long taskId) {
        Intent intentToStartAddTaskActivity =
                new Intent(MainActivity.this, AddTaskActivity.class);
        Uri uriForTaskClicked = TodoListContract.TaskEntry.buildUriWithId(taskId);
        intentToStartAddTaskActivity.setData(uriForTaskClicked);
        startActivity(intentToStartAddTaskActivity);
    }
}
