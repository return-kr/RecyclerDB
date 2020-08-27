package com.example.recyclerdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
    TextView nTitle, nDate, nTime, nDetail;
    int _id;
    String id;
    String name, date, time, detail;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        nTitle = findViewById(R.id.nTitle);
        nDate = findViewById(R.id.nDate);
        nTime = findViewById(R.id.nTime);
        nDetail = findViewById(R.id.nDetail);
        delete = findViewById(R.id.nDelBtn);

        Intent intent = getIntent();
        _id = intent.getIntExtra("_id", 0);
        name = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        detail = intent.getStringExtra("detail");

        id = String.valueOf(_id);

        nTitle.setText(name);
        nDate.setText(date);
        nTime.setText(time);
        nDetail.setText(detail);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cnfDlg();
            }
        });
    }

    void cnfDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBHelper db = new DBHelper(NotificationActivity.this);
                db.deleteRow(id);
                Intent _int = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(_int);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent1 = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        builder.create().show();
    }
}