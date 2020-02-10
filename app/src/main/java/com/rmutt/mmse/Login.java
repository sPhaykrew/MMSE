package com.rmutt.mmse;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button CF = findViewById(R.id.CF);
        final EditText mmse_ID = findViewById(R.id.mmse_id);

        //isReadStoragePermissionGranted();

        final boolean ss = isWriteStoragePermissionGranted();

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        editor.clear(); // clear data
        editor.apply();

        CF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mmse_ID.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"กรุณาพิมพ์รหัสอาสา",Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("mmse_ID",mmse_ID.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(Login.this,Main.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
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

}
