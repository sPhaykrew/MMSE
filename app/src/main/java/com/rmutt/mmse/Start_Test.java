package com.rmutt.mmse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rmutt.mmse.RecyclerView.Patient_Model;
import com.rmutt.mmse.Tests.No_1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Start_Test extends AppCompatActivity {

    Spinner spinner_education,spinner_calculate,spinner_checktest,spiner_where;
    EditText edit_id,edit_name,edit_age;
    String[] education_list = {"","ไม่ได้เรียนหนังสือ","ประถมศึกษา","สูงกว่าประถมศึกษา"};
    String[] calculate_list = {"","เป็น","ไม่เป็น"};
    String[] checktest_list = {"","ใช่","ไม่ใช่"};
    String[] where_list = {"","โรงพยาบาล","บ้าน"};
    String education,calculate,checktest,where;
    Button next;
    int education_position = 0;
    int calculate_position = 0;
    int checktest_position = 0;
    int where_position = 0;
    int pk_auto = 0;
    Patient_Model patient;
    ArrayList<String> get_date_time = new ArrayList<>();
    String mmse_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_test);

        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        TextView Title = toolbar.findViewById(R.id.title_sub);
        Title.setText("รายการผู้ป่วย");
        ImageView back = toolbar.findViewById(R.id.back);
        next = findViewById(R.id.next);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final SharedPreferences sp_pk = getSharedPreferences("Patient_PK_auto", Context.MODE_PRIVATE);
        pk_auto = sp_pk.getInt("PK_auto", 0);

        final SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String patient_PK = sp.getString("Patient_PK", "null");

        SharedPreferences sp_mmse_ID = getSharedPreferences("MMSE", Context.MODE_PRIVATE);
        mmse_ID = sp_mmse_ID.getString("mmse_ID", "null");

        final Database database = new Database(this);
        patient = database.patient(patient_PK);

        edit_id = findViewById(R.id.edit_ID);
        edit_name = findViewById(R.id.edit_name);
        edit_age = findViewById(R.id.edit_age);

        spinner_education = findViewById(R.id.edit_education);
        spinner_calculate = findViewById(R.id.edit_calculate);
        spinner_checktest = findViewById(R.id.edit_checktest);
        spiner_where = findViewById(R.id.edit_where);

        ArrayAdapter adapter_education = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,education_list);
        ArrayAdapter adapter_calculate = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,calculate_list);
        ArrayAdapter adapter_checktest = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,checktest_list);
        ArrayAdapter adapter_where = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,where_list);

        spinner_education.setAdapter(adapter_education);
        spinner_calculate.setAdapter(adapter_calculate);
        spinner_checktest.setAdapter(adapter_checktest);
        spiner_where.setAdapter(adapter_where);

        edit_id.setText(patient.getPatient_ID());
        edit_name.setText(patient.getName());
        edit_age.setText(String.valueOf(patient.getAge()));

        final Patient_Model patient_model = database.patient(patient_PK);

        String sum_date_time;
        get_date_time = date_time(); //get real date_time

        if (patient.getTime() == null) {
             sum_date_time = get_date_time.get(0)+"/"+get_date_time.get(5)+"/"+get_date_time.get(3)+" "+get_date_time.get(4);
        } else {
            sum_date_time = patient.getTime(); //get old date_time
        }

        if (!patient_PK.equals("null")){
            edit_id.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้
        }

        if (patient.getEducation() != null) {
            switch (patient.getEducation()) {
                case "ไม่ได้เรียนหนังสือ":
                    education_position = 1;
                    break;
                case "ประถมศึกษา":
                    education_position = 2;
                    break;
                case "สูงกว่าประถมศึกษา":
                    education_position = 3;
                    break;
            }
        }

        if (patient.getCalculate() != null) {
            switch (patient.getCalculate()) {
                case "เป็น":
                    calculate_position = 1;
                    break;
                case "ไม่เป็น":
                    calculate_position = 2;
                    break;
            }
        }

        if (patient.getCheck_test() != null) {
            switch (patient.getCheck_test()) {
                case "ใช่":
                    checktest_position = 1;
                    break;
                case "ไม่ใช่":
                    checktest_position = 2;
                    break;
            }
        }

        if (patient.getWhere() != null) {
            switch (patient.getWhere()) {
                case "โรงพยาบาล":
                    where_position = 1;
                    break;
                case "บ้าน":
                    where_position = 2;
                    break;
            }
        }

        spinner_education.setSelection(education_position);
        spinner_calculate.setSelection(calculate_position);
        spinner_checktest.setSelection(checktest_position);
        spiner_where.setSelection(where_position);

        spinner_education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                education = spinner_education.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_calculate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculate = spinner_calculate.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_checktest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checktest = spinner_checktest.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spiner_where.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                where = spiner_where.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String finalSum_date_time = sum_date_time;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_id.getText().toString().equals("") || edit_name.getText().toString().equals("") || //ตรวจว่ากรอกข้อมูลครบหรือไม่
                        edit_age.getText().toString().equals("0") || education.equals("") || calculate.equals("") ||
                        checktest.equals("") || where.equals("")){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else {

                    if (patient_PK.equals("null")){

                        pk_auto = pk_auto+1; //กำหนด pk เอง เวลาเพิ่มผู้ป่วยซ้ำจะได้ระบุ pk เองได้
                        String pk_auto_sum = edit_id.getText().toString() + "_" + pk_auto;
                        final SharedPreferences.Editor editor_pk_auto = sp_pk.edit();
                        editor_pk_auto.putInt("PK_auto",pk_auto);
                        editor_pk_auto.apply();

                        database.insert_patient(edit_id.getText().toString(),edit_name.getText().toString(),
                                Integer.parseInt(edit_age.getText().toString()),education,calculate,checktest
                                ,where, finalSum_date_time,"ทำต่อ",pk_auto_sum);

                        //set test_ID mmse_yymmddhhmm
                        String test_ID = mmse_ID+"_"+get_date_time.get(3)+get_date_time.get(5)+get_date_time.get(0)+get_date_time.get(6);

                        database.insert_patient_id_test(pk_auto_sum,test_ID); //เพิ่ม id ที่หน้า table test ไม่งั้นจะ error

                        //เวลาเพิ่มผู้ป่วยเอง หน้าต่อไปจะหา patient_id ไม่เจอเลยต้องเคลียแล้วรับค่าจาก edit_id แทนแล้วเซฟใหม่
                        SharedPreferences.Editor editor = sp.edit();

                        editor.clear(); // clear data
                        editor.apply();

                        editor.putString("Patient_PK",pk_auto_sum);
                        editor.commit();

                    } else {
                        database.update_patient(patient_PK,edit_name.getText().toString(),Integer.parseInt(edit_age.getText().toString()),
                                education,calculate,checktest,where, finalSum_date_time);

                        String test_ID = database.test_ID(patient_PK);
                        String update_test_ID = mmse_ID+"_"+get_date_time.get(3)+get_date_time.get(5)+get_date_time.get(0)+get_date_time.get(6);
                        if (test_ID == null){
                            database.update_test_ID(patient_PK,update_test_ID);
                        }

                        if (patient_model.getStatus().equals("เริ่มทำ")){
                            database.update_patient_status(patient_PK,"ทำต่อ");
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), No_1.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

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

}
