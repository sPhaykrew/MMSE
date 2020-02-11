package com.rmutt.mmse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.rmutt.mmse.Export_Import.Import_Export;

public class Main extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    int import_patient_code = 12;
    Import_Export Import;
    String mmse_ID;
    int pk_auto = 0;
    SharedPreferences sp_pk;

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
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                popupMenu.inflate(R.menu.menu_main);
                popupMenu.setOnMenuItemClickListener(Main.this);
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
                startActivityForResult(import_patient,import_patient_code);

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
//                Import_Export import_export = new Import_Export(getApplicationContext());
//                import_export.export_test_data("6");
//                import_export.export_patient_data("6",mmse_ID,"ssssss");
                return true;


            default: return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        String path = String.valueOf(data.getData());
        Log.e("Pathb", path);
        pk_auto = pk_auto+1; //กำหนด pk เอง เวลาเพิ่มผู้ป่วยซ้ำจะได้ระบุ pk เองได้
        Import.import_csv(Uri.parse(path), String.valueOf(pk_auto)); //ดูได้หน้า start test
        final SharedPreferences.Editor editor_pk_auto = sp_pk.edit();
        editor_pk_auto.putInt("PK_auto",pk_auto);
        editor_pk_auto.apply();
    }

}
