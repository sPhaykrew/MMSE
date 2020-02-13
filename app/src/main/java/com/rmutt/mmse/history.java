package com.rmutt.mmse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class history extends AppCompatActivity {

    TextView patient_ID,patient_name,patient_age,patient_education,time,status;
    LinearLayout color_status;
    Button history_button;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    Drive googleDriveService;
    String folderId;
    String fileid;
    Database database;
    String patient_PK;
    String mmse_ID;
    ArrayList<getFolder_model> getFolder_models = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        TextView Title = toolbar.findViewById(R.id.title_sub);
        Title.setText("รายการผู้ป่วย");
        ImageView back = toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        patient_ID = findViewById(R.id.patient_ID);
        patient_name = findViewById(R.id.patient_name);
        patient_age = findViewById(R.id.patient_age);
        patient_education = findViewById(R.id.patient_education);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        color_status = findViewById(R.id.color_status);
        history_button = findViewById(R.id.history_button);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        patient_PK = sp.getString("Patient_PK", "null");

        SharedPreferences mmse_sp = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        mmse_ID = mmse_sp.getString("mmse_ID", "null");

        database = new Database(getApplicationContext());
        final Patient_Model patient = database.patient(patient_PK);

        patient_ID.setText("หมายเลขผู้ป่วย : " + patient.getPatient_ID());
        patient_name.setText("ชื่อ : " + patient.getName());
        patient_age.setText("อายุ : " + patient.getAge());
        patient_education.setText("ระดับการศึกษา : " + patient.getEducation());
        time.setText("วันที่ทำ : " + patient.getTime());
        status.setText("สถานะ : " + patient.getStatus());
        color_status.setBackgroundColor(patient.getStatus_color());

        switch (patient.getStatus()){
            case "เริ่มทำ" : history_button.setText("เริ่มทำ"); break;
            case "ทำต่อ" : history_button.setText("ทำต่อ"); break;
            case "พร้อมส่ง" : history_button.setText("ส่งข้อมูล"); break;
        }

        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (patient.getStatus()){
                    case "เริ่มทำ" :
                        Intent go_StartTest = new Intent(getApplicationContext(),Start_Test.class);
                        startActivity(go_StartTest);
                        finish();
                        break;
                    case "ทำต่อ" :
                        Intent go_QuestionList = new Intent(getApplicationContext(),Question_list.class);
                        startActivity(go_QuestionList);
                        finish();
                        break;
                    case "พร้อมส่ง" :
                        wait_upload();
                        break;
                }
            }
        });
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
                ProgressDialog progressDialog = new ProgressDialog(history.this);
                progressDialog.setTitle("กำลังส่งข้อมูล");
                progressDialog.setMessage("โปรดรอ...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                find_folder().addOnSuccessListener(new OnSuccessListener<String>() { //ค้นหาโฟรเดอร์ทั้งหมดในกุเกิลไดร
                    @Override
                    public void onSuccess(String s) {
                        progressDialog.dismiss();


                        ProgressDialog progressDialog = new ProgressDialog(history.this);
                        progressDialog.setTitle("กำลังส่งข้อมูล");
                        progressDialog.setMessage("โปรดรอ...");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        searchFolder_andDelete(googleDriveService,getFolder_models,folderName).addOnSuccessListener(new OnSuccessListener<String>() { //ตรวจสอบว่ามีชื่อโฟรเดอร์ซ้ำกันก่อนหน้าไหม ถ้ามีจะลบโฟรเดอร์ออกก่อนเพื่อป้องกันไม่ให้สร้างโฟรเดอร์ซ้ำซ้อน
                            @Override
                            public void onSuccess(String s) {
                                progressDialog.dismiss();

                                ProgressDialog progressDialog = new ProgressDialog(history.this);
                                progressDialog.setTitle("กำลังส่งข้อมูล");
                                progressDialog.setMessage("โปรดรอ...");
                                progressDialog.setCancelable(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                                create_folder(folderName).addOnSuccessListener(new OnSuccessListener<String>() { //สร้างโฟรเดอร์
                                    @Override
                                    public void onSuccess(String s) {
                                        progressDialog.dismiss();

                                        ProgressDialog progressDialog = new ProgressDialog(history.this);
                                        progressDialog.setTitle("กำลังส่งข้อมูล");
                                        progressDialog.setMessage("โปรดรอ...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();

                                        upload_file().addOnSuccessListener(new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                progressDialog.dismiss();
                                                shearfile();
                                                database.delete_patient(patient_PK); //ลบผู้ใช้งาน
                                                finish();
                                                Toast.makeText(getApplicationContext(),"ส่งข้อมูลเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                deleteFolder(googleDriveService,folderId);
                                                Toast.makeText(getApplicationContext(),"เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่",Toast.LENGTH_SHORT).show();
                                            }
                                        }); //upload file
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        deleteFolder(googleDriveService, folderId);
                                        Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "เกิดข้อผิดพลาด กรุณากดส่งข้อมูลใหม่", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "กรุณาล็อคอินกุเกิลไดรไดรฟ์ก่อนส่งข้อมูล", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "กรุณาเชื่อมต่ออินเตอร์เน็ต", Toast.LENGTH_SHORT).show();
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

            ArrayList<uploadFile_model> uploadFile_models = new ArrayList<>();

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

    public Task<String> searchFolder_andDelete(Drive service,ArrayList<getFolder_model> models,String folderUpload_Name) { //delete folder in google drive
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
