package com.example.recyclerdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    DBHelper db;
//    ArrayList<String> t_id, t_name, t_date, t_time, t_detail;

    private List<TaskModel> model = new ArrayList<>();

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        db = new DBHelper(MainActivity.this);
//        t_id = new ArrayList<>();
//        t_name = new ArrayList<>();
//        t_date = new ArrayList<>();
//        t_time = new ArrayList<>();
//        t_detail = new ArrayList<>();

        //customAdapter = new CustomAdapter(MainActivity.this, t_id, t_name, t_date, t_time, t_detail);
        customAdapter = new CustomAdapter(this, model);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        storeDataInArrays();

        customAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

    }

    void storeDataInArrays() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                model.add(new TaskModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4))
                );
//                t_id.add(cursor.getString(0));
//                t_name.add(cursor.getString(1));
//                t_date.add(cursor.getString(2));
//                t_time.add(cursor.getString(3));
//                t_detail.add(cursor.getString(4));
            }
        }
    }
}