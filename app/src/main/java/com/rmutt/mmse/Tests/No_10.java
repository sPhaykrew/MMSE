package com.rmutt.mmse.Tests;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.rmutt.mmse.Database;
import com.rmutt.mmse.Question_list;
import com.rmutt.mmse.R;
import com.rmutt.mmse.RecyclerView.Patient_Model;
import com.rmutt.mmse.Split;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class No_10 extends AppCompatActivity {

    RadioGroup radioGroup10_1;
    TextView question;
    Button next,before;
    Dialog dialog_back;
    ImageView take_picture;
    private static final int permission_code = 100;
    Uri image_uri;
    Bitmap bitmap;
    int sumscore = 0;
    String checkradio10_1 = "";
    String answer;
    String mmse_ID;
    String Patient_PK;
    Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_10);

        dialog_back = new Dialog(this);

        final Toolbar toolbar = findViewById(R.id.toolbar_sub);
        TextView Title = toolbar.findViewById(R.id.title_sub);
        Title.setText("รายการคำถาม");
        final ImageView back = toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_back.setContentView(R.layout.back_dialog);
                Button cf = dialog_back.findViewById(R.id.cf);
                Button cancel = dialog_back.findViewById(R.id.cancel);

                cf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Question_list.class);
                        startActivity(intent);
                        finish();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_back.dismiss();
                    }
                });
                dialog_back.show();
            }
        });

        next = findViewById(R.id.next);
        before = findViewById(R.id.before);
        take_picture = findViewById(R.id.take_picture);
        question = findViewById(R.id.question);
        radioGroup10_1 = findViewById(R.id.radiogroup10_1);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        Patient_PK = sp.getString("Patient_PK", "null");

        SharedPreferences sp_mmse = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        mmse_ID = sp_mmse.getString("mmse_ID", "null");

        database = new Database(getApplicationContext());
        Patient_Model patient_model = database.patient(Patient_PK);

        Split split = new Split();

        question.setText("คุณ "+split.get_FirstName(patient_model.getName())+" เขียนข้อความอะไรก็ได้ที่อ่านแล้วรู้เรื่อง หรือมีความหมายมา 1 ประโยค");

        final ArrayList<String> get_no10 = database.get_no10(Patient_PK);
        if (get_no10.get(0) != null){

            if (get_no10.get(0).equals("ถูก")){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton) radioGroup10_1.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton) radioGroup10_1.getChildAt(1)).setChecked(true);
            }

//            for (int i = 0; i < radioGroup10_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup10_1.getChildAt(i).setEnabled(false);
//            }

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            answer = get_no10.get(0);
            sumscore = Integer.parseInt(get_no10.get(2)); // get ค่า score

            File file = new File(get_no10.get(1)); // โหลด path picture แล้ว set picture
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            take_picture.setImageBitmap(bitmap);

            //take_picture.setEnabled(false);
        }

        radioGroup10_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio10_1_correct :
                        checkradio10_1 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio10_1_wrong :
                        if (checkradio10_1.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }
                        checkradio10_1 = "wrong";
                        break;
                }
            }
        });

        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWriteStoragePermissionGranted();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null || radioGroup10_1.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else {
                    if (checkradio10_1.equals("correct")) {
                        answer = "ถูก";
                    } else if (checkradio10_1.equals("wrong")) {
                        answer = "ผิด";
                    }
                    String path_image = export_image(convertBitmapIntoByteArray());
                    database.update_no10(Patient_PK,answer,path_image,sumscore);

                    Intent go_no11 = new Intent(getApplicationContext(),No_11.class);
                    startActivity(go_no11);
                    finish();
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_9 = new Intent(getApplicationContext(),No_9.class);
                startActivity(go_9);
                finish();
            }
        });
    }

    private void openCamera() {
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera,permission_code);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        image_uri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, permission_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case permission_code:
                if (requestCode == permission_code)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), image_uri);
                            take_picture.setImageBitmap(bitmap);
                            //imageurl = getRealPathFromURI(image_uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
        }
    }

    private byte[] convertBitmapIntoByteArray() { //ลดขนาดรูป แปลงรูป
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);//ขนาดภาพที่ลดลง
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    private String export_image(byte[] data){

        String file_name = null;

        //date_time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //สร้างรูปใน My_MMSE
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MMSE/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdir();
            file.canExecute();
        }
        try {
            file_name = directory_path  + database.test_ID(Patient_PK) + "_10" + ".jpeg";
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file_name));
            fileOutputStream.write(data);
            fileOutputStream.close();
            //Toast.makeText(getApplicationContext(), "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return file_name;
    }

    @Override
    public void onBackPressed() { // เกิดเมื่อเวลากดปุ่ม back ที่เครื่อง
        dialog_back.setContentView(R.layout.back_dialog);
        Button cf = dialog_back.findViewById(R.id.cf);
        Button cancel = dialog_back.findViewById(R.id.cancel);

        cf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Question_list.class);
                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_back.dismiss();
            }
        });
        dialog_back.show();
    }

    public  boolean isReadStoragePermissionGranted() { //ต้องขอทีละ permission
        String TAG = "Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() { //ต้องขอทีละ permission
        String TAG = "Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                openCamera();
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //เอาทำงานต่อเลยหลังจากกดยอมรับ permission
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 2 :
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
            break;
        }
    }
}
