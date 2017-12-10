package com.dinkapach.android.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Din on 12/9/2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskAdapterViewHolder> {

    private String[] mTaskData;

    @Override
    public TaskAdapter.TaskAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TaskAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TaskAdapterViewHolder extends RecyclerView.ViewHolder{
        public final TextView mTaskTitleTextView, mTaskDateTextView;

        public TaskAdapterViewHolder(View itemView) {
            super(itemView);
            mTaskDateTextView = itemView.findViewById(R.id.tv_list_task_item_due_date);
            mTaskTitleTextView = itemView.findViewById(R.id.tv_list_task_item_title);
        }
    }
}
