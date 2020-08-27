package com.example.recyclerdb;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText tName, tDetail;
    ImageView imageView;
    TextView tDate, tTime;
    Button dateBtn, timeBtn, tCancel, tCreate, imgBtn;
    int tHour, tMint, tempDate, tempYear, tempMonth;
    String enTime, enDate;

    final int REQUEST_CODE_GALLERY = 999;

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
        imgBtn = findViewById(R.id.imgBtn);
        imageView = findViewById(R.id.tImage);

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
                if (enDate == null || enTime == null) {
                    Toast.makeText(AddActivity.this, "Date and Time must be set", Toast.LENGTH_SHORT).show();
                } else {
                    DBHelper db = new DBHelper(AddActivity.this);
                    byte[] imageEntry = imageViewToByte(imageView);
                    db.addTask(tName.getText().toString().trim(),
                            enDate,
                            enTime,
                            tDetail.getText().toString().trim(),
                            imageEntry);
                    Intent _int = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(_int);
                    finish();
                }
            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(AddActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        return stream.toByteArray();
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
                if (minute < 10)
                    enTime = hour + ":" + "0" + minute;
                else
                    enTime = hour + ":" + minute;
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
        tempYear = year;
        tempDate = date;
        tempMonth = month;
        if (date < 10 && month < 10)
            enDate = "0" + date + "/" + "0" + (month + 1) + "/" + year;
        else if (date < 10 || month < 10) {
            if (date < 10)
                enDate = "0" + date + "/" + (month + 1) + "/" + year;
            if (month < 10)
                enDate = date + "/" + "0" + (month + 1) + "/" + year;
        } else
            enDate = date + "/" + (month + 1) + "/" + year;
        tDate.setText(enDate);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "You don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                assert uri != null;
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}