package com.rmutt.mmse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.rmutt.mmse.RecyclerView.Patient_Model;

public class history extends AppCompatActivity {

    TextView patient_ID,patient_name,patient_age,patient_education,time,status;
    LinearLayout color_status;
    Button history_button;

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
        String ID = sp.getString("Patient_ID", "null");

        Database database = new Database(getApplicationContext());
        final Patient_Model patient = database.patient(ID);

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
            case "ส่งแล้ว" : history_button.setText("ส่งข้อมูล"); break;
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
                    case "ส่งแล้ว" : break;
                }
            }
        });
    }
}
