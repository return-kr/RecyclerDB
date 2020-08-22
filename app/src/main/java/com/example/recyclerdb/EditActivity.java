package com.example.recyclerdb;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import static com.example.recyclerdb.R.id.editDate;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText name, detail;
    TextView eDate, eTime;
    Button update, cancel, dateBtn, timeBtn;
    String edDate, edTime;
    int eHour, eMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        name = findViewById(R.id.editName);
        detail = findViewById(R.id.editDetails);

        eDate = findViewById(editDate);
        eTime = findViewById(R.id.editTime);

        update = findViewById(R.id.editUpdate);
        cancel = findViewById(R.id.editCancel);
        dateBtn = findViewById(R.id.dateBtnEdt);
        timeBtn = findViewById(R.id.timeBtnEdt);

        Intent intent = getIntent();

        final String id = intent.getStringExtra("id");
        final String Name = intent.getStringExtra("name");
        final String Date = intent.getStringExtra("date");
        final String Time = intent.getStringExtra("time");
        final String Detail = intent.getStringExtra("detail");

        edDate = Date;
        edTime = Time;

        name.setText(Name);
        eDate.setText(Date);
        eTime.setText(Time);
        detail.setText(Detail);

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempName = name.getText().toString().trim();
                String tempDetail = detail.getText().toString().trim();
                DBHelper db = new DBHelper(EditActivity.this);
                db.updateData(id, tempName, edDate, edTime, tempDetail);
                Intent it = new Intent(EditActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                eHour = hour;
                eMinute = minute;
                edTime = hour + " : " + minute + "";
                eTime.setText(edTime);
            }
        };
        new TimePickerDialog(EditActivity.this, timeSetListener,
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
        edDate = date + "/" + (month + 1) + "/" + year + "";
        eDate.setText(edDate);
    }
}