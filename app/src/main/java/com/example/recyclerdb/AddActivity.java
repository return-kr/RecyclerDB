package com.example.recyclerdb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText tName, tDetail;
    TextView tDate, tTime;
    Button dateBtn, timeBtn, tCancel, tCreate;
    int tHour, tMint;
    String enTime, enDate;  // Final Date and Time picker variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        tName = findViewById(R.id.tName);
        tDetail = findViewById(R.id.tDetails);
        tDate = findViewById(R.id.tDate);
        tTime = findViewById(R.id.tTime);
        dateBtn = findViewById(R.id.dateBtn);
        timeBtn = findViewById(R.id.timeBtn);
        tCancel = findViewById(R.id.tCancel);
        tCreate = findViewById(R.id.tCreate);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        tCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper db = new DBHelper(AddActivity.this);
                db.addTask(tName.getText().toString().trim(),
                        enDate,
                        enTime,
                        tDetail.getText().toString().trim());
            }
        });
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                tHour = hour;
                tMint = minute;
                enTime = hour + " : " + minute + "";
                tTime.setText(enTime);
            }
        };
        new TimePickerDialog(AddActivity.this, timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        enDate = date + "/" + (month + 1) + "/" + year + "";
        tDate.setText(enDate);
    }
}