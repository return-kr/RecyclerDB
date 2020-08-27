package com.example.recyclerdb;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    DBHelper db;
    ImageView img;
    TextView txt;

    int id_no;
    String tempName;
    String tempDetail;
    String channelId;
    String tempDate;
    String tempTime;
    String stringDate;
    Date fDate, cDate;

    private List<TaskModel> model = new ArrayList<>();

    CustomAdapter customAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        img = findViewById(R.id.notFound);
        txt = findViewById(R.id.notData);

        db = new DBHelper(MainActivity.this);
        customAdapter = new CustomAdapter(this, model);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        customAdapter.notifyDataSetChanged();
        storeDataInArrays();

        createNotificationChannel();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Notification";
            String description = "My Task Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notifyChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    void storeDataInArrays() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            img.setVisibility(View.VISIBLE);
            txt.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                model.add(new TaskModel(
                        id_no = cursor.getInt(0),
                        tempName = cursor.getString(1),
                        tempDate = cursor.getString(2),
                        tempTime = cursor.getString(3),
                        tempDetail = cursor.getString(4),
                        cursor.getBlob(5))
                );
                img.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
                stringDate = tempDate + " " + tempTime;
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    fDate = format.parse(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("channelId", channelId);
                intent.putExtra("_id", id_no);
                intent.putExtra("title", tempName);
                intent.putExtra("date", tempDate);
                intent.putExtra("time", tempTime);
                intent.putExtra("detail", tempDetail);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id_no, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long timeAtBtnClick = System.currentTimeMillis();
                cDate = new Date();
                long timeMillis = fDate.getTime() - cDate.getTime();
                if (timeMillis <= 0) {
                    return;
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtBtnClick + timeMillis, pendingIntent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cust_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All ?");
        builder.setMessage("Are you sure want to delete all tasks ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBHelper db = new DBHelper(MainActivity.this);
                db.deleteAllData();
                Intent _int = new Intent(MainActivity.this, MainActivity.class);
                startActivity(_int);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        appExitDlg();
    }

    protected void appExitDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Confirm!");
        builder.setMessage("Do you want to exit ?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "See You Soon!", Toast.LENGTH_SHORT).show();
//                MainActivity.super.onBackPressed();
                finishAffinity();
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