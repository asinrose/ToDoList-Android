package com.example.todolist;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAddTask;
    ListView listViewTasks;
    TextView txtNoTasks;
    DatabaseHelper databaseHelper;
    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddTask = findViewById(R.id.btnAddTask);
        listViewTasks = findViewById(R.id.listViewTasks);
        txtNoTasks = findViewById(R.id.txtNoTasks);
        databaseHelper = new DatabaseHelper(this);

        btnAddTask.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddTaskActivity.class
                    );

            startActivity(intent);
        });

        loadTasks();
    }

    private void loadTasks() {

        List<Task> taskList =
                databaseHelper.getAllTasks();

        if (taskList.isEmpty()) {

            txtNoTasks.setVisibility(TextView.VISIBLE);
            listViewTasks.setVisibility(ListView.GONE);

        } else {

            txtNoTasks.setVisibility(TextView.GONE);
            listViewTasks.setVisibility(ListView.VISIBLE);

            taskAdapter =
                    new TaskAdapter(
                            this,
                            taskList
                    );

            listViewTasks.setAdapter(taskAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadTasks();
    }
}