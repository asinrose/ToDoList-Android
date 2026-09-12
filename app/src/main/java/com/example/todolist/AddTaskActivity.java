package com.example.todolist;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    EditText edtTaskName;
    Spinner spinnerPriority;
    Button btnSaveTask;

    DatabaseHelper databaseHelper;

    // Used to identify whether we are adding or editing
    int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Connect XML components
        edtTaskName = findViewById(R.id.edtTaskName);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        // Database
        databaseHelper = new DatabaseHelper(this);

        // Priority options
        String[] priorities = {
                "High",
                "Medium",
                "Low"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        priorities
                );

        spinnerPriority.setAdapter(adapter);

        // Check if we are editing an existing task
        taskId = getIntent().getIntExtra("task_id", -1);

        if (taskId != -1) {

            // Get existing task information
            String taskTitle =
                    getIntent().getStringExtra("task_title");

            String taskPriority =
                    getIntent().getStringExtra("task_priority");

            // Put existing title into EditText
            edtTaskName.setText(taskTitle);

            // Select existing priority
            if (taskPriority != null) {

                int priorityPosition =
                        adapter.getPosition(taskPriority);

                spinnerPriority.setSelection(
                        priorityPosition
                );
            }

            // Change button text
            btnSaveTask.setText("UPDATE TASK");
        }

        // Save / Update button
        btnSaveTask.setOnClickListener(v -> {

            String taskName =
                    edtTaskName.getText()
                            .toString()
                            .trim();

            String priority =
                    spinnerPriority
                            .getSelectedItem()
                            .toString();

            // Check empty task
            if (taskName.isEmpty()) {

                Toast.makeText(
                        AddTaskActivity.this,
                        "Please enter a task",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // -------------------------
            // EDIT EXISTING TASK
            // -------------------------

            if (taskId != -1) {

                boolean updated =
                        databaseHelper.updateTask(
                                taskId,
                                taskName,
                                priority
                        );

                if (updated) {

                    Toast.makeText(
                            AddTaskActivity.this,
                            "Task updated successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();

                } else {

                    Toast.makeText(
                            AddTaskActivity.this,
                            "Failed to update task",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }

            // -------------------------
            // ADD NEW TASK
            // -------------------------

            else {

                boolean success =
                        databaseHelper.addTask(
                                taskName,
                                priority
                        );

                if (success) {

                    Toast.makeText(
                            AddTaskActivity.this,
                            "Task added successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();

                } else {

                    Toast.makeText(
                            AddTaskActivity.this,
                            "Failed to add task",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}