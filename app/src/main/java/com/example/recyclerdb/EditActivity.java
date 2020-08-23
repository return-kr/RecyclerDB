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

import static com.example.recyclerdb.R.id.editDate;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText name, detail;
    TextView eDate, eTime;
    ImageView editImage;
    Button update, cancel, dateBtn, timeBtn, edtImage;
    String edDate, edTime;
    int eHour, eMinute;

    final int REQUEST_CODE_GALLERY = 100;

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
        editImage = findViewById(R.id.editImage);
        edtImage = findViewById(R.id.editImgBtn);

        Intent intent = getIntent();

        final String id = intent.getStringExtra("id");
        final String Name = intent.getStringExtra("name");
        final String Date = intent.getStringExtra("date");
        final String Time = intent.getStringExtra("time");
        final String Detail = intent.getStringExtra("detail");
        final Bitmap Image = intent.getParcelableExtra("image");

        edDate = Date;
        edTime = Time;

        name.setText(Name);
        eDate.setText(Date);
        eTime.setText(Time);
        detail.setText(Detail);
        editImage.setImageBitmap(Image);

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
                byte[] imageEntry = imageViewToByte(editImage);
                DBHelper db = new DBHelper(EditActivity.this);
                db.updateData(id, tempName, edDate, edTime, tempDetail, imageEntry);
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

        edtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(EditActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        return stream.toByteArray();
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
                editImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}