package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "priority TEXT, " +
                "completed INTEGER)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS tasks");

        onCreate(db);
    }

    // Add a new task
    public boolean addTask(String title, String priority) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("priority", priority);
        values.put("completed", 0);

        long result = db.insert("tasks", null, values);

        return result != -1;
    }

    public List<Task> getAllTasks() {

        List<Task> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tasks",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow("title")
                );

                String priority = cursor.getString(
                        cursor.getColumnIndexOrThrow("priority")
                );

                int completed = cursor.getInt(
                        cursor.getColumnIndexOrThrow("completed")
                );

                Task task = new Task(
                        id,
                        title,
                        priority,
                        completed
                );

                taskList.add(task);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return taskList;
    }

    // Update a task
    public boolean updateTask(int id, String title, String priority) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("priority", priority);

        int result = db.update(
                "tasks",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }


    // Delete a task
    public boolean deleteTask(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "tasks",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }


    // Mark task as completed / not completed
    public boolean updateCompleted(int id, int completed) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("completed", completed);

        int result = db.update(
                "tasks",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}