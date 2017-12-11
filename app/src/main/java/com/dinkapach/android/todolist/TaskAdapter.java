package com.dinkapach.android.todolist;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinkapach.android.todolist.data.TodoListContract;

/**
 * Created by Din on 12/9/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskAdapterViewHolder> {

    private final Context mContext;
    private final TaskAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    public TaskAdapter(Context context, TaskAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    public interface TaskAdapterOnClickHandler{
        void onClick(long taskId);
    }

    @Override
    public TaskAdapter.TaskAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToRoot = false;
        View view = inflater.inflate(R.layout.task_view, parent, shouldAttachToRoot);
        return new TaskAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskAdapterViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        String title = mCursor.getString(mCursor.getColumnIndex(TodoListContract.TaskEntry.COLUMN_NAME_TITLE));
        long id = mCursor.getLong(mCursor.getColumnIndex(TodoListContract.TaskEntry._ID));
        holder.itemView.setTag(id);
        holder.mTaskTitleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;
        if (mCursor != null){
            this.notifyDataSetChanged();
        }
    }

    public class TaskAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        public final TextView mTaskTitleTextView;

        public TaskAdapterViewHolder(View itemView) {
            super(itemView);
            mTaskTitleTextView = itemView.findViewById(R.id.tv_list_task_item_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long taskId = mCursor.getLong(mCursor.getColumnIndex(TodoListContract.TaskEntry._ID));
            mClickHandler.onClick(taskId);
        }
    }
}
