package com.rmutt.mmse.Tests;

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

import com.rmutt.mmse.Database;
import com.rmutt.mmse.Question_list;
import com.rmutt.mmse.R;
import com.rmutt.mmse.Split;

import java.util.ArrayList;

public class No_6 extends AppCompatActivity {

    RadioGroup radioGroup6_1,radioGroup6_2;
    EditText edit6_1,edit6_2;
    Button next,before;
    Dialog dialog_back;
    int sumscore = 0;

    String checkradio6_1 = "";
    String checkradio6_2 = "";

    String get_EditText1,get_EditText2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_6);

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
                        Intent intent = new Intent(getApplicationContext(), Question_list.class);
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

        next = findViewById(R.id.next);
        before = findViewById(R.id.before);

        radioGroup6_1 = findViewById(R.id.radiogroup6_1);
        radioGroup6_2 = findViewById(R.id.radiogroup6_2);

        edit6_1 = findViewById(R.id.edit6_1);
        edit6_2 = findViewById(R.id.edit6_2);

//        edit6_1.setFocusable(false);
//        edit6_2.setFocusable(false);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String Patient_PK = sp.getString("Patient_PK", "null");
        final Database database = new Database(getApplicationContext());

        ArrayList<String> get_no6 = database.get_no6(Patient_PK);
        if (get_no6.get(0) != null){
            Split split = new Split();

            if (split.check_answer(get_no6.get(0))){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup6_1.getChildAt(0)).setChecked(true);
                edit6_1.setText(split.get_answer(get_no6.get(0)));
            } else {
                ((RadioButton)radioGroup6_1.getChildAt(1)).setChecked(true);
                edit6_1.setText(split.get_answer(get_no6.get(0)));
            }

            if (split.check_answer(get_no6.get(1))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup6_2.getChildAt(0)).setChecked(true);
                edit6_2.setText(split.get_answer(get_no6.get(1)));
            } else {
                ((RadioButton)radioGroup6_2.getChildAt(1)).setChecked(true);
                edit6_2.setText(split.get_answer(get_no6.get(1)));
            }

//            for (int i = 0; i < radioGroup6_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup6_1.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup6_2.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup6_2.getChildAt(i).setEnabled(false);
//            }


            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no6.get(0);
            get_EditText2 = get_no6.get(1);

            sumscore = Integer.parseInt(get_no6.get(2)); // get ค่า score

        }

        radioGroup6_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio6_1_correct :
                        checkradio6_1 = "correct";
                        edit6_1.setText("ดินสอ");
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio6_1_wrong :
//                        edit6_1.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio6_1.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit6_1.getText().toString().equals("ดินสอ")){
                            edit6_1.setText("");
                        }
                        checkradio6_1 = "wrong";
                        break;
                }
            }
        });

        radioGroup6_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio6_2_correct :
                        checkradio6_2 = "correct";
                        edit6_2.setText("นาฬิกา");
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio6_2_wrong :
//                        edit6_2.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio6_2.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit6_2.getText().toString().equals("นาฬิกา")){
                            edit6_2.setText("");
                        }
                        checkradio6_2 = "wrong";
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup6_1.getCheckedRadioButtonId() == -1 || radioGroup6_2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else if (checkradio6_1.equals("wrong") && edit6_1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else if (checkradio6_2.equals("wrong") && edit6_2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkradio6_1.equals("correct")) {
                        get_EditText1 = "ดินสอ_ถูก";
                    } else if (checkradio6_1.equals("wrong")) {
                        get_EditText1 = edit6_1.getText() + "_ผิด";
                    }

                    if (checkradio6_2.equals("correct")) {
                        get_EditText2 = "นาฬิกา_ถูก";
                    } else if (checkradio6_2.equals("wrong")) {
                        get_EditText2 = edit6_2.getText() + "_ผิด";
                    }

                    database.update_no6(Patient_PK,get_EditText1,get_EditText2,sumscore);

                    Intent go_no7 = new Intent(getApplicationContext(),No_7.class);
                    startActivity(go_no7);
                    finish();
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_5 = new Intent(getApplicationContext(),No_5.class);
                startActivity(go_5);
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
