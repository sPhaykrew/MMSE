package com.rmutt.mmse;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.rmutt.mmse.RecyclerView.RecyclerViewAdapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Main extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Import_Export Import;
    String mmse_ID;
    int pk_auto = 0;
    SharedPreferences sp_pk;
    GoogleSignInAccount alreadyloggedAccount;
    Drive googleDriveService;
    ArrayList<String> get_date_time = new ArrayList<>();
    private ProgressDialog pDialog;
    private Dialog check_internet;
    ViewPager ViewPager;
    TabLayoutAdapter pageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        check_internet = new Dialog(this);
        check_internet.setContentView(R.layout.check_internet_dialog);
        Button cf_internet = check_internet.findViewById(R.id.cf);
        cf_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_internet.dismiss();
            }
        });

        Import = new Import_Export(getApplicationContext());

        SharedPreferences sp = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        mmse_ID = sp.getString("mmse_ID", "null");

        sp_pk = getSharedPreferences("Patient_PK_auto", Context.MODE_PRIVATE);
        pk_auto = sp_pk.getInt("PK_auto", 0);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("รหัส อสม. "+mmse_ID);
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
        ViewPager = findViewById(R.id.viewPager);

        pageAdapter = new TabLayoutAdapter(getSupportFragmentManager(), TabLayout.getTabCount());
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
                finish();
                startActivity(logout);
                return true;

            case R.id.import_patient :

//                Intent import_patient = new Intent(Intent.ACTION_OPEN_DOCUMENT); //Intent find file in directory
//                import_patient.addCategory(Intent.CATEGORY_OPENABLE);
//                import_patient.setType("text/*");
//                startActivityForResult(import_patient,12);

                if(CheckInternet()){
                    isWriteStoragePermissionGranted();
                } else {
                    check_internet.show();
                }

                return true;

//            case R.id.add_patient :
//                SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//
//                editor.clear(); // clear data
//                editor.apply();
//
//                Intent intent = new Intent(this,Start_Test.class);
//                startActivity(intent);
//                return true;

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

//            case 12 : //Intent find file in directory
//                String path = String.valueOf(data.getData());
//                pk_auto = pk_auto+1; //กำหนด pk เอง เวลาเพิ่มผู้ป่วยซ้ำจะได้ระบุ pk เองได้
//                Import.import_csv(Uri.parse(path), String.valueOf(pk_auto)); //ดูได้หน้า start test
//                SharedPreferences.Editor editor_pk_auto = sp_pk.edit();
//                editor_pk_auto.putInt("PK_auto",pk_auto);
//                editor_pk_auto.apply();
//                break;
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

    public ArrayList<String> date_time(){
        // index0 day
        // index1 name of day
        // index2 name of month
        // index3 year
        // index4 time HH:mm
        // index5 month
        // index6 time HHmm

        final String[] MONTH_array= { "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม",
                "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน",
                "ธันวาคม" };

        final String[] DAY_array = {"จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์","อาทิตย์"};

        int Year;
        int Month;
        int Day;
        ArrayList<String> date_time = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH);
        Day = cal.get(Calendar.DAY_OF_MONTH);
        String day = new SimpleDateFormat("u").format(cal.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat time_2 = new SimpleDateFormat("HHmm", Locale.getDefault());

        date_time.add(String.valueOf(Day)); //get day
        date_time.add(DAY_array[Integer.parseInt(day)-1]); //get name of day
        date_time.add(MONTH_array[Month]); //get name of month
        date_time.add(String.valueOf(Year)); //get year
        date_time.add(time.format(new Date())); //get time HH:mm
        date_time.add(String.valueOf(Month+1)); //get month
        date_time.add(time_2.format(new Date())); //get time HHmm

        return date_time;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("กำลังนำเข้าผู้ป่วย... โปรดรอ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String folder_mmse = Environment.getExternalStorageDirectory().getPath() + "/MMSE";
                File file = new File(folder_mmse);
                if (!file.exists()) { //check if not folder create it
                    file.mkdir();
                    file.canExecute();
                }

                // Output stream to write file
                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MMSE/patient.csv";
                OutputStream output = new FileOutputStream(directory_path);
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }



        /**
         * After completing background task
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");
            pDialog.dismiss();

            pk_auto = pk_auto+1; //กำหนด pk เอง เวลาเพิ่มผู้ป่วยซ้ำจะได้ระบุ pk เองได้
            SharedPreferences.Editor editor_pk_auto = sp_pk.edit();
            editor_pk_auto.putInt("PK_auto",pk_auto);
            editor_pk_auto.apply();

//            Import.import_csv(String.valueOf(pk_auto),mmse_ID); //ดูได้หน้า start test
            Import.import_csv(mmse_ID); //ดูได้หน้า start test
            ViewPager.setAdapter(pageAdapter); //reload tablayout
        }

    }

    public  boolean isWriteStoragePermissionGranted() { //ต้องขอทีละ permission
        String TAG = "Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                String url = "https://docs.google.com/spreadsheets/u/1/d/1qoNH5m2JVnteJYb_6mYdUFzDQ1jj-hjcVCASDpjbj3M/export?format=csv".trim();
                new DownloadFileFromURL().execute(url);

                Log.v(TAG,"Permission is granted2");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 53);
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
            case 53 :
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    String url = "https://docs.google.com/spreadsheets/u/1/d/1qoNH5m2JVnteJYb_6mYdUFzDQ1jj-hjcVCASDpjbj3M/export?format=csv".trim();
                    new DownloadFileFromURL().execute(url);
                }
            break;
        }
    }

}
