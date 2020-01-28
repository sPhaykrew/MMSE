package com.rmutt.mmse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class No_1 extends AppCompatActivity {

    RadioGroup radioGroup1_1,radioGroup1_2,radioGroup1_3,radioGroup1_4,radioGroup1_5;
    EditText edit1_1,edit1_2,edit1_3,edit1_4;
    Spinner spinner_season;
    String[] season_list = {"","ฤดูร้อน","ฤดูฝน","ฤดูหนาว"};
    String season = "" ;
    Button next;
    int sumscore = 0;
    int season_position;

    String get_EditText1, get_EditText2, get_EditText3, get_EditText4, get_EditText5;
    String checkradio1_1 = "";
    String checkradio1_2 = "";
    String checkradio1_3 = "";
    String checkradio1_4 = "";
    String checkradio1_5 = "";

    Dialog dialog_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_1);

        dialog_back = new Dialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        TextView Title = toolbar.findViewById(R.id.title_sub);
        Title.setText("รายการคำถาม");
        final ImageView back = toolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_back.setContentView(R.layout.back_dialog);
                Button cf = dialog_back.findViewById(R.id.cf);
                Button cancel = dialog_back.findViewById(R.id.cancel);

                cf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),Question_list.class);
                        startActivity(intent);
                        finish();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_back.dismiss();
                    }
                });

                dialog_back.show();
            }
        });

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String patient_ID = sp.getString("Patient_ID", "null");
        final Database database = new Database(getApplicationContext());

        radioGroup1_1 = findViewById(R.id.radiogroup1_1);
        radioGroup1_2 = findViewById(R.id.radiogroup1_2);
        radioGroup1_3 = findViewById(R.id.radiogroup1_3);
        radioGroup1_4 = findViewById(R.id.radiogroup1_4);
        radioGroup1_5 = findViewById(R.id.radiogroup1_5);

        edit1_1 = findViewById(R.id.edit1_1);
        edit1_2 = findViewById(R.id.edit1_2);
        edit1_3 = findViewById(R.id.edit1_3);
        edit1_4 = findViewById(R.id.edit1_4);

        next = findViewById(R.id.next);

        spinner_season = findViewById(R.id.spinner_season);
        ArrayAdapter adapter_season = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,season_list);
        spinner_season.setAdapter(adapter_season);

        //ถ้าเคยทำไว้แล้วจะ set ตำคอบไว้กับปิดไม่ให้แก้ไข
        ArrayList<String> get_no1 = database.get_no1(patient_ID);
        if (get_no1.get(0) != null){
            Split split = new Split();

            if (split.check_answer(get_no1.get(0))){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup1_1.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup1_1.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no1.get(1))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup1_2.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup1_2.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no1.get(2))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup1_3.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup1_3.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no1.get(3))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup1_4.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup1_4.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no1.get(4))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup1_5.getChildAt(0)).setChecked(true);
                checkradio1_5 = "correct";
            } else {
                ((RadioButton)radioGroup1_5.getChildAt(1)).setChecked(true);
                checkradio1_5 = "wrong";
            }

            for (int i = 0; i < radioGroup1_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
                radioGroup1_1.getChildAt(i).setEnabled(false);
            }

            for (int i = 0; i < radioGroup1_2.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
                radioGroup1_2.getChildAt(i).setEnabled(false);
            }

            for (int i = 0; i < radioGroup1_3.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
                radioGroup1_3.getChildAt(i).setEnabled(false);
            }

            for (int i = 0; i < radioGroup1_4.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
                radioGroup1_4.getChildAt(i).setEnabled(false);
            }

            for (int i = 0; i < radioGroup1_5.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
                radioGroup1_5.getChildAt(i).setEnabled(false);
            }

            edit1_1.setText(split.get_answer(get_no1.get(0)));
            edit1_1.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit1_2.setText(split.get_answer(get_no1.get(1)));
            edit1_2.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit1_3.setText(split.get_answer(get_no1.get(2)));
            edit1_3.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit1_4.setText(split.get_answer(get_no1.get(3)));
            edit1_4.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            if (get_no1.get(4) != null) { //หาค่า position ของ season
                switch (split.get_answer(get_no1.get(4))) {
                    case "ฤดูร้อน":
                        season_position = 1;
                        break;
                    case "ฤดูฝน":
                        season_position = 2;
                        break;
                    case "ฤดูหนาว":
                        season_position = 3;
                        break;
                }
            }
            spinner_season.setSelection(season_position); // set season select
            spinner_season.setEnabled(false);

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no1.get(0);
            get_EditText2 = get_no1.get(1);
            get_EditText3 = get_no1.get(2);
            get_EditText4 = get_no1.get(3);
            get_EditText5 = get_no1.get(4);

            sumscore = Integer.parseInt(get_no1.get(5)); // get ค่า score

        }

        final ArrayList get_date_time = date_time(); //get time

        radioGroup1_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1_1_correct :
                        edit1_1.setText(get_date_time.get(0).toString());
                        edit1_1.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้
                        checkradio1_1 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio1_1_wrong :
                        //edit1_1.setText("");
                        edit1_1.setFocusableInTouchMode(true); // เปิดให้ผู้ใช้แก้ไข edit text ได้
                        if (checkradio1_1.equals("correct")) {
                            sumscore = sumscore - 1;
                        }
                        checkradio1_1 = "wrong";
                        break;
                }
            }
        });

        radioGroup1_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1_2_correct :
                        edit1_2.setText(get_date_time.get(1).toString());
                        edit1_2.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้
                        checkradio1_2 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio1_2_wrong :
                        //edit1_2.setText("");
                        edit1_2.setFocusableInTouchMode(true); // เปิดให้ผู้ใช้แก้ไข edit text ได้
                        if (checkradio1_2.equals("correct")) {
                            sumscore = sumscore - 1;
                        }
                        checkradio1_2 = "wrong";
                        break;
                }
            }
        });

        radioGroup1_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1_3_correct :
                        edit1_3.setText(get_date_time.get(2).toString());
                        edit1_3.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้
                        checkradio1_3 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio1_3_wrong :
                        //edit1_3.setText("");
                        edit1_3.setFocusableInTouchMode(true); // เปิดให้ผู้ใช้แก้ไข edit text ได้
                        if (checkradio1_3.equals("correct")) {
                            sumscore = sumscore - 1;
                        }
                        checkradio1_3 = "wrong";
                        break;
                }
            }
        });

        radioGroup1_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1_4_correct :
                        edit1_4.setText(get_date_time.get(3).toString());
                        edit1_4.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้
                        checkradio1_4= "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio1_4_wrong :
                        //edit1_4.setText("");
                        edit1_4.setFocusableInTouchMode(true); // เปิดให้ผู้ใช้แก้ไข edit text ได้
                        if (checkradio1_4.equals("correct")) {
                            sumscore = sumscore - 1;
                        }
                        checkradio1_4 = "wrong";
                        break;
                }
            }
        });

        radioGroup1_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1_5_correct :
                        checkradio1_5 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio1_5_wrong :
                        if (checkradio1_5.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }
                        checkradio1_5 = "wrong";
                        break;
                        //nothing
                }
            }
        });

        //radioGroup1_5.getCheckedRadioButtonId() == -1 คือการเช็คว่า radiogroup ได้เช็คสักอันหรือไม่
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                season = spinner_season.getSelectedItem().toString();

                if (edit1_1.getText().toString().equals("") || edit1_2.getText().toString().equals("") || //เช็คว่าตอบครบทุกช่องไหมและติ๊กถูกผิดครบไหม
                        edit1_3.getText().toString().equals("") || edit1_4.getText().toString().equals("") || season.equals("")
                        || radioGroup1_1.getCheckedRadioButtonId() == -1 || radioGroup1_2.getCheckedRadioButtonId() == -1
                        || radioGroup1_3.getCheckedRadioButtonId() == -1 || radioGroup1_4.getCheckedRadioButtonId() == -1
                        || radioGroup1_5.getCheckedRadioButtonId() == -1 )
                {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else {

                    if (checkradio1_1.equals("correct")){
                        get_EditText1 = edit1_1.getText() + "_ถูก";
                    }
                    else if (checkradio1_1.equals("wrong"))
                    {
                        get_EditText1 = edit1_1.getText() + "_ผิด";
                    }

                    if (checkradio1_2.equals("correct")){
                        get_EditText2 = edit1_2.getText() + "_ถูก";
                    }
                    else if (checkradio1_2.equals("wrong"))
                    {
                        get_EditText2 = edit1_2.getText() + "_ผิด";
                    }

                    if (checkradio1_3.equals("correct")){
                        get_EditText3 = edit1_3.getText() + "_ถูก";
                    }
                    else if (checkradio1_3.equals("wrong"))
                    {
                        get_EditText3 = edit1_3.getText() + "_ผิด";
                    }

                    if (checkradio1_4.equals("correct")){
                        get_EditText4 = edit1_4.getText() + "_ถูก";
                    }
                    else if (checkradio1_4.equals("wrong"))
                    {
                        get_EditText4 = edit1_4.getText() + "_ผิด";
                    }

                    if (checkradio1_5.equals("correct")){
                        season = spinner_season.getSelectedItem().toString() + "_ถูก";
                    }
                    else if (checkradio1_5.equals("wrong"))
                    {
                        season = spinner_season.getSelectedItem().toString() + "_ผิด";
                    }

                    database.update_no1(patient_ID, get_EditText1, get_EditText2, get_EditText3, get_EditText4,season,sumscore);
                    Patient_Model patient_models = database.patient(patient_ID);

                    if (patient_models.getWhere().equals("โรงพยาบาล"))
                    {
                        Intent go_2_1 = new Intent(getApplicationContext(),No_2_1.class);
                        startActivity(go_2_1);
                        finish();
                    } else if (patient_models.getWhere().equals("บ้าน"))
                    {
                        Intent go_2_2 = new Intent(getApplicationContext(),No_2_2.class);
                        startActivity(go_2_2);
                        finish();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() { // เกิดเมื่อเวลากดปุ่ม back ที่เครื่อง
        dialog_back.setContentView(R.layout.back_dialog);
        Button cf = dialog_back.findViewById(R.id.cf);
        Button cancel = dialog_back.findViewById(R.id.cancel);

        cf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Question_list.class);
                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_back.dismiss();
            }
        });
        dialog_back.show();
    }

    public ArrayList<String> date_time(){
        // index0 day
        // index1 name of day
        // index2 month
        // index3 year
        // index4 time
        // index5 month

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

        date_time.add(String.valueOf(Day)); //get day
        date_time.add(DAY_array[Integer.parseInt(day)-1]); //get name of day
        date_time.add(MONTH_array[Month]); //get month
        date_time.add(String.valueOf(Year)); //get year
        date_time.add(time.format(new Date())); //get time
        date_time.add(String.valueOf(Month+1)); //get month

        return date_time;
    }
}
