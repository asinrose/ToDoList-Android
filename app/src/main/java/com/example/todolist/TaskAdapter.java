package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> taskList;
    private DatabaseHelper databaseHelper;

    public TaskAdapter(Context context, List<Task> taskList) {

        this.context = context;
        this.taskList = taskList;

        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return taskList.get(position).getId();
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context)
                    .inflate(
                            R.layout.task_item,
                            parent,
                            false
                    );
        }

        CheckBox checkBoxCompleted =
                convertView.findViewById(
                        R.id.checkBoxCompleted
                );

        TextView txtTaskTitle =
                convertView.findViewById(
                        R.id.txtTaskTitle
                );

        TextView txtTaskPriority =
                convertView.findViewById(
                        R.id.txtTaskPriority
                );

        Button btnEdit =
                convertView.findViewById(
                        R.id.btnEdit
                );

        Button btnDelete =
                convertView.findViewById(
                        R.id.btnDelete
                );

        Task task = taskList.get(position);

        // Display task information
        txtTaskTitle.setText(task.getTitle());
        txtTaskPriority.setText(
                "Priority: " + task.getPriority()
        );
        if (task.getPriority().equals("High")) {

            txtTaskPriority.setTextColor(
                    android.graphics.Color.RED
            );

        } else if (task.getPriority().equals("Medium")) {

            txtTaskPriority.setTextColor(
                    android.graphics.Color.rgb(255, 152, 0)
            );

        } else {

            txtTaskPriority.setTextColor(
                    android.graphics.Color.rgb(46, 125, 50)
            );
        }
        // Display completed status
        checkBoxCompleted.setChecked(
                task.getCompleted() == 1
        );
        if (task.getCompleted() == 1) {

            txtTaskTitle.setPaintFlags(
                    txtTaskTitle.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG
            );

        } else {

            txtTaskTitle.setPaintFlags(
                    txtTaskTitle.getPaintFlags()
                            & ~Paint.STRIKE_THRU_TEXT_FLAG
            );
        }

        // -------------------------
        // COMPLETE TASK
        // -------------------------

        checkBoxCompleted.setOnCheckedChangeListener(
                null
        );

        checkBoxCompleted.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    int completed;

                    if (isChecked) {
                        completed = 1;
                    } else {
                        completed = 0;
                    }

                    databaseHelper.updateCompleted(
                            task.getId(),
                            completed
                    );

                    task.setCompleted(completed);

                    if (isChecked) {

                        txtTaskTitle.setPaintFlags(
                                txtTaskTitle.getPaintFlags()
                                        | Paint.STRIKE_THRU_TEXT_FLAG
                        );

                    } else {

                        txtTaskTitle.setPaintFlags(
                                txtTaskTitle.getPaintFlags()
                                        & ~Paint.STRIKE_THRU_TEXT_FLAG
                        );
                    }
                }
        );


        // -------------------------
        // DELETE TASK
        // -------------------------

        btnDelete.setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Delete Task")
                    .setMessage(
                            "Are you sure you want to delete this task?"
                    )
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {

                        boolean deleted =
                                databaseHelper.deleteTask(
                                        task.getId()
                                );

                        if (deleted) {

                            taskList.remove(position);

                            notifyDataSetChanged();

                            Toast.makeText(
                                    context,
                                    "Task deleted",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .show();
        });


        // -------------------------
        // EDIT TASK
        // -------------------------

        btnEdit.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            AddTaskActivity.class
                    );

            intent.putExtra(
                    "task_id",
                    task.getId()
            );

            intent.putExtra(
                    "task_title",
                    task.getTitle()
            );

            intent.putExtra(
                    "task_priority",
                    task.getPriority()
            );

            context.startActivity(intent);
        });


        return convertView;
    }
}