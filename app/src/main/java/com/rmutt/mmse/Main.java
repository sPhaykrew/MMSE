package com.rmutt.mmse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.rmutt.mmse.Export_Import.Import_Export;

import java.util.Collections;

public class Main extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Import_Export Import;
    String mmse_ID;
    int pk_auto = 0;
    SharedPreferences sp_pk;
    GoogleSignInAccount alreadyloggedAccount;
    Drive googleDriveService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Import = new Import_Export(getApplicationContext());

        SharedPreferences sp = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        mmse_ID = sp.getString("mmse_ID", "null");

        sp_pk = getSharedPreferences("Patient_PK_auto", Context.MODE_PRIVATE);
        pk_auto = sp_pk.getInt("PK_auto", 0);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("หมายเลขอาสา "+mmse_ID);
        ImageView show_menu = toolbar.findViewById(R.id.show_menu);


        show_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                popupMenu.inflate(R.menu.menu_main);
                popupMenu.setOnMenuItemClickListener(Main.this);

                if (CheckInternet()){
                    if (alreadyloggedAccount != null){
                        popupMenu.getMenu().findItem(R.id.google_drive).setTitle("ออกจากระบบกูเกิลไดรฟ์");
                    }
                } else {
                    popupMenu.getMenu().findItem(R.id.google_drive).setVisible(false); //ซ่อนเมนู
                }


                popupMenu.show();
            }
        });

        TabLayout TabLayout = findViewById(R.id.tablayout);
        TabItem Patient_Data = findViewById(R.id.Patient_Data);
        TabItem Manual = findViewById(R.id.Manual);
        ViewPager ViewPager = findViewById(R.id.viewPager);

        TabLayoutAdapter pageAdapter = new TabLayoutAdapter(getSupportFragmentManager(), TabLayout.getTabCount());
        ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TabLayout));
        TabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(ViewPager));
        ViewPager.setAdapter(pageAdapter);

        TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { //Event when click tablayout

//                if (tab.getPosition() == 1) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(Main.this,
//                            R.color.colorAccent));
//                } else if (tab.getPosition() == 2) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(Main.this,
//                            android.R.color.darker_gray));
//                } else {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(Main.this,
//                            R.color.colorPrimary));
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout :
                Intent logout = new Intent(getApplicationContext(),Login.class);
                startActivity(logout);
                finish();
                return true;

            case R.id.import_patient :
                Intent import_patient = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                import_patient.addCategory(Intent.CATEGORY_OPENABLE);
                import_patient.setType("text/*");
                startActivityForResult(import_patient,12);

                return true;

            case R.id.add_patient :
                SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.clear(); // clear data
                editor.apply();

                Intent intent = new Intent(this,Start_Test.class);
                startActivity(intent);
                return true;

            case R.id.google_drive :

                if (alreadyloggedAccount != null) {
                    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                    GoogleSignInClient googleSignIn = GoogleSignIn.getClient(this,googleSignInOptions);
                    googleSignIn.signOut();}
                else {
                    requestSignIn();
                }

                return true;


            default: return false;
        }
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
            case 12 :
                String path = String.valueOf(data.getData());
                pk_auto = pk_auto+1; //กำหนด pk เอง เวลาเพิ่มผู้ป่วยซ้ำจะได้ระบุ pk เองได้
                Import.import_csv(Uri.parse(path), String.valueOf(pk_auto)); //ดูได้หน้า start test
                SharedPreferences.Editor editor_pk_auto = sp_pk.edit();
                editor_pk_auto.putInt("PK_auto",pk_auto);
                editor_pk_auto.apply();
                break;
        }
    }

    public void requestSignIn(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(new Scope(DriveScopes.DRIVE_FILE)).build();
        GoogleSignInClient client = GoogleSignIn.getClient(this,signInOptions);
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
