package com.rmutt.mmse;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.CompoundButtonCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.rmutt.mmse.Export_Import.Import_Export;
import com.rmutt.mmse.Export_Import.getFolder_model;
import com.rmutt.mmse.Export_Import.uploadFile_model;
import com.rmutt.mmse.RecyclerView.Patient_Model;
import com.rmutt.mmse.Tests.No_1;
import com.rmutt.mmse.Tests.No_10;
import com.rmutt.mmse.Tests.No_11;
import com.rmutt.mmse.Tests.No_2_1;
import com.rmutt.mmse.Tests.No_2_2;
import com.rmutt.mmse.Tests.No_3;
import com.rmutt.mmse.Tests.No_4_1;
import com.rmutt.mmse.Tests.No_4_2;
import com.rmutt.mmse.Tests.No_5;
import com.rmutt.mmse.Tests.No_6;
import com.rmutt.mmse.Tests.No_7;
import com.rmutt.mmse.Tests.No_8;
import com.rmutt.mmse.Tests.No_9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Question_list extends AppCompatActivity {

    CheckBox no1,no2,no3,no4,no5,no6,no7,no8,no9,no10,no11;
    LinearLayout question_1,question_2,question_3,question_4,question_5,question_6,question_7,question_8,question_9,question_10,question_11;
    Button next;
    ArrayList<String> get_no1,get_no2,get_no3,get_no4,get_no5,get_no6,get_no7,get_no8,get_no9,get_no10,get_no11;
    TextView no4_text_1,no4_text_2,no9_text_1,no9_text_2,no10_text_1,no10_text_2;
    String mmse_ID;
    Patient_Model patient_model;

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    Drive googleDriveService;
    String folderId;
    String fileid;
    Database database;
    String patient_PK;
    ArrayList<uploadFile_model> uploadFile_models = new ArrayList<>();
    ArrayList<getFolder_model> getFolder_models = new ArrayList<>();
    Dialog check_internet,check_GD,upload_error,finish_upload;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list);

        check_internet = new Dialog(this);
        check_internet.setContentView(R.layout.check_internet_dialog);
        Button cf_internet = check_internet.findViewById(R.id.cf);
        cf_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_internet.dismiss();
            }
        });


        check_GD = new Dialog(this);
        check_GD.setContentView(R.layout.check_gd_dialog);
        Button cf_GD = check_GD.findViewById(R.id.cf);
        cf_GD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_GD.dismiss();
            }
        });

        upload_error = new Dialog(this);
        upload_error.setContentView(R.layout.upload_error_dialog);
        Button cf_upload_error = upload_error.findViewById(R.id.cf);
        cf_upload_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_error.dismiss();
            }
        });

        finish_upload = new Dialog(this);
        finish_upload.setContentView(R.layout.finish_uplod_dialog);
        Button cf_finish_upload = finish_upload.findViewById(R.id.cf);
        cf_finish_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_upload.dismiss();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        TextView Title = toolbar.findViewById(R.id.title_sub);
        Title.setText("รายการผู้ป่วย");
        final ImageView back = toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next = findViewById(R.id.next);

        no1 = findViewById(R.id.no1);
        no2 = findViewById(R.id.no2);
        no3 = findViewById(R.id.no3);
        no4 = findViewById(R.id.no4);
        no5 = findViewById(R.id.no5);
        no6 = findViewById(R.id.no6);
        no7 = findViewById(R.id.no7);
        no8 = findViewById(R.id.no8);
        no9 = findViewById(R.id.no9);
        no10 = findViewById(R.id.no10);
        no11 = findViewById(R.id.no11);

        question_1 = findViewById(R.id.question_1);
        question_2 = findViewById(R.id.question_2);
        question_3 = findViewById(R.id.question_3);
        question_4 = findViewById(R.id.question_4);
        question_5 = findViewById(R.id.question_5);
        question_6 = findViewById(R.id.question_6);
        question_7 = findViewById(R.id.question_7);
        question_8 = findViewById(R.id.question_8);
        question_9 = findViewById(R.id.question_9);
        question_10 = findViewById(R.id.question_10);
        question_11 = findViewById(R.id.question_11);

        no4_text_1 = findViewById(R.id.no4_text_1);
        no4_text_2 = findViewById(R.id.no4_text_2);
        no9_text_1 = findViewById(R.id.no9_text_1);
        no9_text_2 = findViewById(R.id.no9_text_2);
        no10_text_1 = findViewById(R.id.no10_text_1);
        no10_text_2 = findViewById(R.id.no10_text_2);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        patient_PK = sp.getString("Patient_PK", "null");

        SharedPreferences mmse_sp = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        mmse_ID = mmse_sp.getString("mmse_ID", "null");

        database = new Database(getApplicationContext());
        patient_model = database.patient(patient_PK);

        get_no1 = database.get_no1(patient_PK);
        get_no2 = database.get_no2(patient_PK);
        get_no3 = database.get_no3(patient_PK);
        get_no4 = database.get_no4(patient_PK);
        get_no5 = database.get_no5(patient_PK);
        get_no6 = database.get_no6(patient_PK);
        get_no7 = database.get_no7(patient_PK);
        get_no8 = database.get_no8(patient_PK);
        get_no9 = database.get_no9(patient_PK);
        get_no10 = database.get_no10(patient_PK);
        get_no11 = database.get_no11(patient_PK);

        if (patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
            int states[][] = {{android.R.attr.state_checked}, {}}; //เปลี่ยนสี checkbox
            int colors[] = {Color.parseColor("#808080"), Color.parseColor("#808080")};

            no4_text_1.setTextColor(Color.parseColor("#808080"));
            no4_text_2.setTextColor(Color.parseColor("#808080"));
            no4_text_2.setText("ทดสอบสมาธิ(ยกเว้น)");
            CompoundButtonCompat.setButtonTintList(no4, new ColorStateList(states, colors));

            no9_text_1.setTextColor(Color.parseColor("#808080"));
            no9_text_2.setTextColor(Color.parseColor("#808080"));
            no9_text_2.setText("ทดสอบการอ่านการเข้าใจความหมาย สามารถทำตามได้(ยกเว้น)");
            CompoundButtonCompat.setButtonTintList(no9, new ColorStateList(states, colors));

            no10_text_1.setTextColor(Color.parseColor("#808080"));
            no10_text_2.setTextColor(Color.parseColor("#808080"));
            no10_text_2.setText("ทดสอบการเขียนภาษาอย่างมีความหมาย(ยกเว้น)");
            CompoundButtonCompat.setButtonTintList(no10, new ColorStateList(states, colors));


        }

        final boolean get_check_answer = check_answer();
        if (get_check_answer){
            next.setText("ส่งข้อมูล");
        }

        question_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no1.get(0) != null) {
                    Intent no1 = new Intent(getApplicationContext(), No_1.class);
                    startActivity(no1);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no2.get(0) != null ){
                    switch (patient_model.getWhere()){
                        case "โรงพยาบาล" :
                            Intent no_2_1 = new Intent(getApplicationContext(), No_2_1.class);
                            startActivity(no_2_1);
                            finish();
                            break;
                        case "บ้าน" :
                            Intent no_2_2 = new Intent(getApplicationContext(), No_2_2.class);
                            startActivity(no_2_2);
                            finish();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no3.get(0) != null) {
                    Intent no3 = new Intent(getApplicationContext(), No_3.class);
                    startActivity(no3);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no4.get(0) != null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                    switch (patient_model.getCalculate()){
                        case "เป็น" :
                            Intent no4_1 = new Intent(getApplicationContext(), No_4_1.class);
                            startActivity(no4_1);
                            finish();
                            break;
                        case "ไม่เป็น" :
                            Intent no4_2 = new Intent(getApplicationContext(), No_4_2.class);
                            startActivity(no4_2);
                            finish();
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no5.get(0) != null) {
                    Intent no5 = new Intent(getApplicationContext(), No_5.class);
                    startActivity(no5);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no6.get(0) != null) {
                    Intent no6 = new Intent(getApplicationContext(), No_6.class);
                    startActivity(no6);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no7.get(0) != null) {
                    Intent no7 = new Intent(getApplicationContext(), No_7.class);
                    startActivity(no7);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no8.get(0) != null) {
                    Intent no8 = new Intent(getApplicationContext(), No_8.class);
                    startActivity(no8);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no9.get(0) != null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                    Intent no9 = new Intent(getApplicationContext(), No_9.class);
                    startActivity(no9);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no10.get(0) != null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                    Intent no10 = new Intent(getApplicationContext(), No_10.class);
                    startActivity(no10);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        question_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (get_no11.get(0) != null) {
                    Intent no11 = new Intent(getApplicationContext(), No_11.class);
                    startActivity(no11);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"ยังไม่ได้ทำแบบทดสอบกรุณากดปุ่มถัดไป",Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (get_check_answer){
                    //Toast.makeText(getApplicationContext(),"ส่งข้อมูล",Toast.LENGTH_SHORT).show();
                    wait_upload();
                } else {
                    if (get_no1.get(0) == null) {
                        Intent go_no1 = new Intent(getApplicationContext(),No_1.class);
                        startActivity(go_no1);
                        finish();
                    } else if (get_no2.get(0) == null ){
                        switch (patient_model.getWhere()){
                            case "โรงพยาบาล" :
                                Intent go_2_1 = new Intent(getApplicationContext(),No_2_1.class);
                                startActivity(go_2_1);
                                finish();
                                break;
                            case "บ้าน" :
                                Intent go_2_2 = new Intent(getApplicationContext(),No_2_2.class);
                                startActivity(go_2_2);
                                finish();
                                break;
                        }
                    } else if (get_no3.get(0) == null) {
                        Intent go_no3 = new Intent(getApplicationContext(),No_3.class);
                        startActivity(go_no3);
                        finish();
                    } else if (get_no4.get(0) == null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                        switch (patient_model.getCalculate()){
                            case "เป็น" :
                                Intent go_no4_1 = new Intent(getApplicationContext(),No_4_1.class);
                                startActivity(go_no4_1);
                                finish();
                                break;
                            case "ไม่เป็น" :
                                Intent go_no4_2 = new Intent(getApplicationContext(),No_4_2.class);
                                startActivity(go_no4_2);
                                finish();
                                break;
                        }
                    } else if (get_no5.get(0) == null){
                        Intent go_no5 = new Intent(getApplicationContext(),No_5.class);
                        startActivity(go_no5);
                        finish();
                    } else if (get_no6.get(0) == null){
                        Intent go_no6 = new Intent(getApplicationContext(),No_6.class);
                        startActivity(go_no6);
                        finish();
                    } else if (get_no7.get(0) == null){
                        Intent go_no7 = new Intent(getApplicationContext(),No_7.class);
                        startActivity(go_no7);
                        finish();
                    } else if (get_no8.get(0) == null){
                        Intent go_no8 = new Intent(getApplicationContext(),No_8.class);
                        startActivity(go_no8);
                        finish();
                    } else if (get_no9.get(0) == null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                        Intent go_no9 = new Intent(getApplicationContext(),No_9.class);
                        startActivity(go_no9);
                        finish();
                    } else if (get_no10.get(0) == null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                        Intent go_no10 = new Intent(getApplicationContext(),No_10.class);
                        startActivity(go_no10);
                        finish();
                    } else if (get_no11.get(0) == null){
                        Intent go_no11 = new Intent(getApplicationContext(),No_11.class);
                        startActivity(go_no11);
                        finish();
                    }
                }
            }
        });

    }

    public boolean check_answer() { //เช็คว่าทำแบบทดสอบไปแล้วหรือยัง

        boolean check_finish = false;

        if (get_no1.get(0) != null) {
            no1.setChecked(true);
        }

        if (get_no2.get(0) != null) {
            no2.setChecked(true);
        }

        if (get_no3.get(0) != null) {
            no3.setChecked(true);
        }

        if (get_no4.get(0) != null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
            no4.setChecked(true);
        }

        if (get_no5.get(0) != null){
            no5.setChecked(true);
        }

        if (get_no6.get(0) != null){
            no6.setChecked(true);
        }

        if (get_no7.get(0) != null){
            no7.setChecked(true);
        }

        if (get_no8.get(0) != null){
            no8.setChecked(true);
        }

        if (get_no9.get(0) != null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
            no9.setChecked(true);
        }

        if (get_no10.get(0) != null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
            no10.setChecked(true);
        }

        if (get_no11.get(0) != null){
            no11.setChecked(true);
            check_finish = true;
        }

        no1.setEnabled(false);
        no2.setEnabled(false);
        no3.setEnabled(false);
        no4.setEnabled(false);
        no5.setEnabled(false);
        no6.setEnabled(false);
        no7.setEnabled(false);
        no8.setEnabled(false);
        no9.setEnabled(false);
        no10.setEnabled(false);
        no11.setEnabled(false);

        return check_finish;
    }

    public void wait_upload(){
        if (CheckInternet()) {
            GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            if (alreadyloggedAccount != null) {
                //Toast.makeText(getApplicationContext(), "Already Logged In", Toast.LENGTH_SHORT).show();

                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
                credential.setSelectedAccount(alreadyloggedAccount.getAccount());
                googleDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).setApplicationName("MMSE Drive").build();

                //export
                Import_Export import_export = new Import_Export(getApplicationContext());
                import_export.export_test_data(patient_PK);
                import_export.export_patient_data(patient_PK, mmse_ID);

                String folderName = database.test_ID(patient_PK); //get test id

                //upload google drive
                ProgressDialog progressDialog = new ProgressDialog(Question_list.this);
                progressDialog.setTitle("กำลังส่งข้อมูล");
                progressDialog.setMessage("โปรดรอ...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                find_folder().addOnSuccessListener(new OnSuccessListener<String>() { //ค้นหาโฟรเดอร์ทั้งหมดในกุเกิลไดร
                    @Override
                    public void onSuccess(String s) {
                        progressDialog.dismiss();


                        ProgressDialog progressDialog = new ProgressDialog(Question_list.this);
                        progressDialog.setTitle("กำลังส่งข้อมูล");
                        progressDialog.setMessage("โปรดรอ...");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        searchFolder_andDelete(googleDriveService,getFolder_models,folderName).addOnSuccessListener(new OnSuccessListener<String>() { //ตรวจสอบว่ามีชื่อโฟรเดอร์ซ้ำกันก่อนหน้าไหม ถ้ามีจะลบโฟรเดอร์ออกก่อนเพื่อป้องกันไม่ให้สร้างโฟรเดอร์ซ้ำซ้อน
                            @Override
                            public void onSuccess(String s) {
                                progressDialog.dismiss();

                                ProgressDialog progressDialog = new ProgressDialog(Question_list.this);
                                progressDialog.setTitle("กำลังส่งข้อมูล");
                                progressDialog.setMessage("โปรดรอ...");
                                progressDialog.setCancelable(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                                create_folder(folderName).addOnSuccessListener(new OnSuccessListener<String>() { //สร้างโฟรเดอร์
                                    @Override
                                    public void onSuccess(String s) {
                                        progressDialog.dismiss();

                                        ProgressDialog progressDialog = new ProgressDialog(Question_list.this);
                                        progressDialog.setTitle("กำลังส่งข้อมูล");
                                        progressDialog.setMessage("โปรดรอ...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();

                                        upload_file().addOnSuccessListener(new OnSuccessListener<String>() { //up load file
                                            @Override
                                            public void onSuccess(String s) {
                                                progressDialog.dismiss();

                                                ProgressDialog progressDialog = new ProgressDialog(Question_list.this);
                                                progressDialog.setTitle("กำลังส่งข้อมูล");
                                                progressDialog.setMessage("โปรดรอ...");
                                                progressDialog.setCancelable(false);
                                                progressDialog.setCanceledOnTouchOutside(false);
                                                progressDialog.show();

                                                shearfile().addOnSuccessListener(new OnSuccessListener<String>() { //แชร์ข้อมูลไปให้ไดรหลัก
                                                    @Override
                                                    public void onSuccess(String s) {
                                                        progressDialog.dismiss();

                                                        for (int i=0;i<uploadFile_models.size();i++){
                                                            java.io.File file = new java.io.File(uploadFile_models.get(i).getFile_path());
                                                            file.delete();
                                                        }
                                                        database.delete_patient(patient_PK); //ลบผู้ใช้งาน
                                                        finish();
                                                        finish_upload.show();
//                                                        Toast.makeText(getApplicationContext(),"ส่งข้อมูลเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        deleteFolder(googleDriveService,folderId);
                                                        upload_error.show();
//                                                        Toast.makeText(getApplicationContext(),"เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                deleteFolder(googleDriveService,folderId);
                                                upload_error.show();
//                                                Toast.makeText(getApplicationContext(),"เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่",Toast.LENGTH_SHORT).show();
                                            }
                                        }); //upload file
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        deleteFolder(googleDriveService, folderId);
                                        upload_error.show();
//                                        Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                upload_error.show();
//                                Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        upload_error.show();
//                        Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                check_GD.show();
//                Toast.makeText(getApplicationContext(), "กรุณาล็อคอินกุเกิลไดรไดรฟ์ก่อนส่งข้อมูล", Toast.LENGTH_SHORT).show();
            }
        } else {
            check_internet.show();
//            Toast.makeText(getApplicationContext(), "กรุณาเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_SHORT).show();
        }
    }

    public Task<String> create_folder(String forlderName) {
        return Tasks.call(mExecutor,()-> {
            File fileMetadata = new File();
            fileMetadata.setName(forlderName); //ตั่งชื่อ folder ใน google drive
            fileMetadata.setMimeType("application/vnd.google-apps.folder");
            File file = null;
            try {
                file = googleDriveService.files().create(fileMetadata)
                        .setFields("id")
                        .execute();
                System.out.println("Folder ID: " + file.getId());
                folderId = file.getId();
            } catch (Exception e){
                throw  new IOException("Null result");
            }
            return file.getId();
        });}

    public Task<String> shearfile(){
        return Tasks.call(mExecutor,()-> {
            Permission newPermission = new Permission();
            newPermission.setEmailAddress("mmse.thai@gmail.com");
            newPermission.setType("user");
            newPermission.setRole("writer");
            try {
                googleDriveService.permissions().create(folderId, newPermission).execute();
                //Toast.makeText(getApplicationContext(),"Done Shared successfully!!!!!!",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
            }
//            return fileid;
            return folderId;
        });}

    public Task<String> upload_file(){
        return Tasks.call(mExecutor,()-> {

            String test_ID = database.test_ID(patient_PK); //get test id
            ArrayList no_10 = database.get_no10(patient_PK);
            ArrayList no_11 = database.get_no11(patient_PK);
            Split split = new Split();

            //get file patient data
            uploadFile_model getFile = new uploadFile_model();
            getFile.setFile_path(database.get_patient_data_path(patient_PK));
            getFile.setFile_name(test_ID +"_" + "ข้อมูลผู้ป่วย" + ".csv");
            getFile.setType("text/csv");
            uploadFile_models.add(getFile);

            //get file test data
            getFile = new uploadFile_model();
            getFile.setFile_path(database.get_test_data_path(patient_PK));
            getFile.setFile_name(test_ID +"_" + "ผลการทำแบบทดสอบ" + ".csv");
            getFile.setType("text/csv");
            uploadFile_models.add(getFile);

            //get image no10
            getFile = new uploadFile_model();
            getFile.setFile_path(String.valueOf(no_10.get(1)));
            getFile.setFile_name(split.get_nameImage(String.valueOf(no_10.get(1))));
            getFile.setType("image/jpeg");
            uploadFile_models.add(getFile);

            //get image no11
            getFile = new uploadFile_model();
            getFile.setFile_path(String.valueOf(no_11.get(1)));
            getFile.setFile_name(split.get_nameImage(String.valueOf(no_11.get(1))));
            getFile.setType("image/jpeg");
            uploadFile_models.add(getFile);
            File myFile = null;

            for (int i=0;i<uploadFile_models.size();i++) {

                File fileMetaData = new File();
                fileMetaData.setName(uploadFile_models.get(i).getFile_name());
                fileMetaData.setParents(Collections.singletonList(folderId));
                java.io.File file = new java.io.File(uploadFile_models.get(i).getFile_path());
//            FileContent mediaContent = new FileContent("text/csv",file);
                FileContent mediaContent = new FileContent(uploadFile_models.get(i).getType(), file);

                try {
                    myFile = googleDriveService.files().create(fileMetaData, mediaContent).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (myFile == null) {
                    throw new IOException("Null result");
                }
            }
            fileid = myFile.getId();
            return myFile.getId();
        });
    }

    public Task<String> deleteFolder(Drive service, String fileId_) { //delete folder in google drive
        return Tasks.call(mExecutor,()-> {
            try {
                service.files().delete(fileId_).execute();
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
            }
            return "delete successfully";
        });
    }

    public Task<String> searchFolder_andDelete(Drive service, ArrayList<getFolder_model> models, String folderUpload_Name) { //delete folder in google drive
        return Tasks.call(mExecutor,()-> {
            for (int i=0;i<models.size();i++) {
                if (models.get(i).getFolder_name().equals(folderUpload_Name)){
                    try {
                        service.files().delete(models.get(i).getFolder_ID()).execute();
                    } catch (IOException e) {
                        System.out.println("An error occurred: " + e);
                    }
                } else {
                }
            }
            return "delete successfully";
        });
    }

    public Task<String> find_folder(){
        return Tasks.call(mExecutor,()->{
            String pageToken = null;
            do {
                FileList result = null;
                try {
                    result = googleDriveService.files().list()
                            .setQ("mimeType='application/vnd.google-apps.folder'")
                            .setSpaces("drive")
                            .setFields("nextPageToken, files(id, name)")
                            .setPageToken(pageToken)
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (File file : result.getFiles()) {
                    getFolder_model get_folder = new getFolder_model();
                    Log.d("getname",file.getName());
                    Log.d("getID",file.getId());
                    get_folder.setFolder_name(file.getName());
                    get_folder.setFolder_ID(file.getId());
                    getFolder_models.add(get_folder);
                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
            return "Find folder";
        });
    }

    public boolean CheckInternet(){
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;
    }

}
