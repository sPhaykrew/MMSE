package com.rmutt.mmse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Main extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String mmse_ID = sp.getString("mmse_ID", "null");

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        TextView Title = toolbar.findViewById(R.id.title);
        Title.setText("หมายเลขอาสาสมัคร "+mmse_ID);
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
                Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this,"4",Toast.LENGTH_SHORT).show();
                return true;


            default: return false;
        }
    }
}
