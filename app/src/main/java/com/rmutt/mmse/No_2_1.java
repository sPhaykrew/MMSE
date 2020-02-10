package com.rmutt.mmse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class No_2_1 extends AppCompatActivity {

    RadioGroup radioGroup2_1,radioGroup2_2,radioGroup2_3,radioGroup2_4,radioGroup2_5;
    EditText edit2_1,edit2_2,edit2_3,edit2_4,edit2_5;
    Button next,before;
    Dialog dialog_back;
    int sumscore = 0;

    String checkradio2_1 = "";
    String checkradio2_2 = "";
    String checkradio2_3 = "";
    String checkradio2_4 = "";
    String checkradio2_5 = "";

    String get_EditText1,get_EditText2,get_EditText3,get_EditText4,get_EditText5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_2_1);

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

        radioGroup2_1 = findViewById(R.id.radiogroup2_1_1);
        radioGroup2_2 = findViewById(R.id.radiogroup2_1_2);
        radioGroup2_3 = findViewById(R.id.radiogroup2_1_3);
        radioGroup2_4 = findViewById(R.id.radiogroup2_1_4);
        radioGroup2_5 = findViewById(R.id.radiogroup2_1_5);

        edit2_1 = findViewById(R.id.edit2_1_1);
        edit2_2 = findViewById(R.id.edit2_1_2);
        edit2_3 = findViewById(R.id.edit2_1_3);
        edit2_4 = findViewById(R.id.edit2_1_4);
        edit2_5 = findViewById(R.id.edit2_1_5);

        next = findViewById(R.id.next);
        before = findViewById(R.id.before);

        //ถ้าเคยทำไว้แล้วจะ set ตำคอบไว้กับปิดไม่ให้แก้ไข
        ArrayList<String> get_no2 = database.get_no2(patient_ID);
        if (get_no2.get(0) != null){
            Split split = new Split();

            if (split.check_answer(get_no2.get(0))){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup2_1.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup2_1.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no2.get(1))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup2_2.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup2_2.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no2.get(2))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup2_3.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup2_3.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no2.get(3))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup2_4.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup2_4.getChildAt(1)).setChecked(true);
            }

            if (split.check_answer(get_no2.get(4))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup2_5.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton)radioGroup2_5.getChildAt(1)).setChecked(true);
            }

//            for (int i = 0; i < radioGroup2_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup2_1.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup2_2.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup2_2.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup2_3.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup2_3.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup2_4.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup2_4.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup2_5.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup2_5.getChildAt(i).setEnabled(false);
//            }

            edit2_1.setText(split.get_answer(get_no2.get(0)));
            //edit2_1.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit2_2.setText(split.get_answer(get_no2.get(1)));
            //edit2_2.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit2_3.setText(split.get_answer(get_no2.get(2)));
            //edit2_3.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit2_4.setText(split.get_answer(get_no2.get(3)));
            //edit2_4.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit2_5.setText(split.get_answer(get_no2.get(3)));
            //edit2_5.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no2.get(0);
            get_EditText2 = get_no2.get(1);
            get_EditText3 = get_no2.get(2);
            get_EditText4 = get_no2.get(3);
            get_EditText5 = get_no2.get(4);

            sumscore = Integer.parseInt(get_no2.get(5)); // get ค่า score

        }

        radioGroup2_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio2_1_1_correct :
                        checkradio2_1 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio2_1_1_wrong :
                        if (checkradio2_1.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }
                        checkradio2_1 = "wrong";
                        break;
                }
            }
        });

        radioGroup2_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio2_1_2_correct :
                        checkradio2_2 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio2_1_2_wrong :
                        if (checkradio2_2.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }
                        checkradio2_2 = "wrong";
                        break;
                }
            }
        });

        radioGroup2_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio2_1_3_correct :
                        checkradio2_3 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio2_1_3_wrong :
                        if (checkradio2_3.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }
                        checkradio2_3 = "wrong";
                        break;
                }
            }
        });

        radioGroup2_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio2_1_4_correct :
                        checkradio2_4 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio2_1_4_wrong :
                        if (checkradio2_4.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }
                        checkradio2_4 = "wrong";
                        break;
                }
            }
        });

        radioGroup2_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio2_1_5_correct :
                        checkradio2_5 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio2_1_5_wrong :
                        if (checkradio2_5.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }
                        checkradio2_5 = "wrong";
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                get_EditText1 = edit2_1.getText().toString();
//                get_EditText2 = edit2_2.getText().toString();
//                get_EditText3 = edit2_3.getText().toString();
//                get_EditText4 = edit2_4.getText().toString();
//                get_EditText5 = edit2_5.getText().toString();

                if (edit2_1.getText().toString().equals("") || edit2_2.getText().toString().equals("") || edit2_3.getText().toString().equals("") ||
                edit2_4.getText().toString().equals("") || edit2_5.getText().toString().equals("") || radioGroup2_1.getCheckedRadioButtonId() == -1
                || radioGroup2_2.getCheckedRadioButtonId() == -1 || radioGroup2_3.getCheckedRadioButtonId() == -1
                || radioGroup2_4.getCheckedRadioButtonId() == -1 || radioGroup2_5.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else {

                    if (checkradio2_1.equals("correct")){
                        get_EditText1 = edit2_1.getText() + "_ถูก";
                    }
                    else if (checkradio2_1.equals("wrong"))
                    {
                        get_EditText1 = edit2_1.getText() + "_ผิด";
                    }

                    if (checkradio2_2.equals("correct")){
                        get_EditText2 = edit2_2.getText() + "_ถูก";
                    }
                    else if (checkradio2_2.equals("wrong"))
                    {
                        get_EditText2 = edit2_2.getText() + "_ผิด";
                    }

                    if (checkradio2_3.equals("correct")){
                        get_EditText3 = edit2_3.getText() + "_ถูก";
                    }
                    else if (checkradio2_3.equals("wrong"))
                    {
                        get_EditText3 = edit2_3.getText() + "_ผิด";
                    }

                    if (checkradio2_4.equals("correct")){
                        get_EditText4 = edit2_4.getText() + "_ถูก";
                    }
                    else if (checkradio2_4.equals("wrong"))
                    {
                        get_EditText4 = edit2_4.getText() + "_ผิด";
                    }

                    if (checkradio2_5.equals("correct")){
                        get_EditText5 = edit2_5.getText() + "_ถูก";
                    }
                    else if (checkradio2_5.equals("wrong"))
                    {
                        get_EditText5 = edit2_5.getText() + "_ผิด";
                    }

                    database.update_no2(patient_ID,get_EditText1,get_EditText2,get_EditText3,get_EditText4,get_EditText5,sumscore);

                    Intent go_no3 = new Intent(getApplicationContext(),No_3.class);
                    startActivity(go_no3);
                    finish();
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_no1 = new Intent(getApplicationContext(),No_1.class);
                startActivity(go_no1);
                finish();
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

}
