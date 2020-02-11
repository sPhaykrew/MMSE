package com.rmutt.mmse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.CompoundButtonCompat;

import com.rmutt.mmse.Export_Import.Import_Export;
import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;

public class Question_list extends AppCompatActivity {

    CheckBox no1,no2,no3,no4,no5,no6,no7,no8,no9,no10,no11;
    LinearLayout question_1,question_2,question_3,question_4,question_5,question_6,question_7,question_8,question_9,question_10,question_11;
    Button next;
    ArrayList<String> get_no1,get_no2,get_no3,get_no4,get_no5,get_no6,get_no7,get_no8,get_no9,get_no10,get_no11;
    TextView no4_text_1,no4_text_2,no9_text_1,no9_text_2,no10_text_1,no10_text_2;
    String mmse_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list);

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
        final String Patient_PK = sp.getString("Patient_PK", "null");

        SharedPreferences mmse_sp = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        mmse_ID = mmse_sp.getString("mmse_ID", "null");

        Database database = new Database(getApplicationContext());
        final Patient_Model patient_model = database.patient(Patient_PK);

        get_no1 = database.get_no1(Patient_PK);
        get_no2 = database.get_no2(Patient_PK);
        get_no3 = database.get_no3(Patient_PK);
        get_no4 = database.get_no4(Patient_PK);
        get_no5 = database.get_no5(Patient_PK);
        get_no6 = database.get_no6(Patient_PK);
        get_no7 = database.get_no7(Patient_PK);
        get_no8 = database.get_no8(Patient_PK);
        get_no9 = database.get_no9(Patient_PK);
        get_no10 = database.get_no10(Patient_PK);
        get_no11 = database.get_no11(Patient_PK);

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
                    Intent no1 = new Intent(getApplicationContext(),No_1.class);
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
                            Intent no_2_1 = new Intent(getApplicationContext(),No_2_1.class);
                            startActivity(no_2_1);
                            finish();
                            break;
                        case "บ้าน" :
                            Intent no_2_2 = new Intent(getApplicationContext(),No_2_2.class);
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
                    Intent no3 = new Intent(getApplicationContext(),No_3.class);
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
                            Intent no4_1 = new Intent(getApplicationContext(),No_4_1.class);
                            startActivity(no4_1);
                            finish();
                            break;
                        case "ไม่เป็น" :
                            Intent no4_2 = new Intent(getApplicationContext(),No_4_2.class);
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
                    Intent no5 = new Intent(getApplicationContext(),No_5.class);
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
                    Intent no6 = new Intent(getApplicationContext(),No_6.class);
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
                    Intent no7 = new Intent(getApplicationContext(),No_7.class);
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
                    Intent no8 = new Intent(getApplicationContext(),No_8.class);
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
                if (get_no9.get(0) == null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                    Intent no9 = new Intent(getApplicationContext(),No_9.class);
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
                if (get_no10.get(0) == null && !patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")){
                    Intent no10 = new Intent(getApplicationContext(),No_10.class);
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
                    Intent no11 = new Intent(getApplicationContext(),No_11.class);
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
                    Import_Export import_export = new Import_Export(getApplicationContext());
                    import_export.export_test_data(Patient_PK);
                    import_export.export_patient_data(Patient_PK,mmse_ID);
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

        if (get_no4.get(0) != null){
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

        if (get_no9.get(0) != null){
            no9.setChecked(true);
        }

        if (get_no10.get(0) != null){
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

}
