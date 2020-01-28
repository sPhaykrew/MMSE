package com.rmutt.mmse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;

public class Question_list extends AppCompatActivity {

    CheckBox no1,no2,no3,no4,no5,no6,no7,no8,no9,no10,no11;
    Button next;
    ArrayList<String> get_no1,get_no2,get_no3,get_no4,get_no5,get_no6,get_no7,get_no8,get_no9,get_no10,get_no11;

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

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String patient_id = sp.getString("Patient_ID", "null");

        Database database = new Database(getApplicationContext());
        final Patient_Model patient_model = database.patient(patient_id);

        get_no1 = database.get_no1(patient_id);
        get_no2 = database.get_no2(patient_id);
        get_no3 = database.get_no3(patient_id);
        get_no4 = database.get_no4(patient_id);
        get_no5 = database.get_no5(patient_id);
        get_no6 = database.get_no6(patient_id);
        get_no7 = database.get_no7(patient_id);
        get_no8 = database.get_no8(patient_id);
        get_no9 = database.get_no9(patient_id);
        get_no10 = database.get_no10(patient_id);
        get_no11 = database.get_no11(patient_id);

        final boolean get_check_answer = check_answer();

        if (get_check_answer){
            next.setText("ส่งข้อมูล");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (get_check_answer){
                    Toast.makeText(getApplicationContext(),"ส่งข้อมูล",Toast.LENGTH_SHORT).show();
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
