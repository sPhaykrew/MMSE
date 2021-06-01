package com.rmutt.mmse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

public class Login extends AppCompatActivity {

    Drive googleDriveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button CF = findViewById(R.id.CF);
        final EditText mmse_ID = findViewById(R.id.mmse_id);

        //final boolean ss = isWriteStoragePermissionGranted();

        SharedPreferences sp = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        editor.clear(); // clear data
        editor.apply();

        if (CheckInternet()){
            requestSignIn();
            Log.d("check_internet","true");
        } else {
            Log.d("check_internet","fail");
        }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 48 :
                if (resultCode == RESULT_OK){
                handleSignInIntent(data);
                }
                break;
        }
    }

    public void requestSignIn(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(new Scope(DriveScopes.DRIVE_FILE)).build();
        GoogleSignInClient client = GoogleSignIn.getClient(getApplicationContext(),signInOptions);
        startActivityForResult(client.getSignInIntent(),48);
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data).addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
            @Override
            public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(),
                        Collections.singleton(DriveScopes.DRIVE_FILE));

                credential.setSelectedAccount(googleSignInAccount.getAccount());

                googleDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(),new GsonFactory(),credential)
                        .setApplicationName("MMSE Drive").build();

//                driveServiceHelper = new DriveServiceHelper(googleDriveService);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
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
