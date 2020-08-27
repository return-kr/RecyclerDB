package com.example.recyclerdb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    TextView name, date, time, detail;
    Button update, delete;
    ImageView imageView;

    String edName, edID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        name = findViewById(R.id.viewTitle);
        date = findViewById(R.id.viewDate);
        time = findViewById(R.id.viewTime);
        detail = findViewById(R.id.viewDetail);
        imageView = findViewById(R.id.viewImage);

        update = findViewById(R.id.viewEditBtn);
        delete = findViewById(R.id.viewDelBtn);

        Intent intent = getIntent();

        final String id = intent.getStringExtra("id");
        final String Name = intent.getStringExtra("name");
        final String Date = intent.getStringExtra("date");
        final String Time = intent.getStringExtra("time");
        final String Detail = intent.getStringExtra("detail");
        final Bitmap Image = intent.getParcelableExtra("image");

        edName = Name;
        edID = id;

        name.setText(Name);
        date.setText(Date);
        time.setText(Time);
        detail.setText(Detail);
        imageView.setImageBitmap(Image);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(Name);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateActivity.this, EditActivity.class);
                i.putExtra("id", id);
                i.putExtra("name", Name);
                i.putExtra("date", Date);
                i.putExtra("time", Time);
                i.putExtra("detail", Detail);
                i.putExtra("image", Image);
                startActivity(i);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + edName + " ?");
        builder.setMessage("Are you sure want to delete " + edName + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBHelper db = new DBHelper(UpdateActivity.this);
                db.deleteRow(edID);
                Intent _int = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(_int);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

}