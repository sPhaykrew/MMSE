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
import com.rmutt.mmse.RecyclerView.Patient_Model;
import com.rmutt.mmse.Split;

import java.util.ArrayList;

public class No_7 extends AppCompatActivity {

    RadioGroup radioGroup7_1;
    EditText edit7_1;
    Button next,before;
    Dialog dialog_back;
    int sumscore = 0;

    String checkradio7_1 = "";
    String get_EditText1;

    TextView question;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_7);

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
                        dialog_back.dismiss();
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

        radioGroup7_1 = findViewById(R.id.radiogroup7_1);
        edit7_1 = findViewById(R.id.edit7_1);

        question = findViewById(R.id.question);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String Patient_PK = sp.getString("Patient_PK", "null");
        final Database database = new Database(getApplicationContext());

        Patient_Model patient_model = database.patient(Patient_PK);
        Split split = new Split();

        question.setText("ตั้งใจฟัง ผม/ดิฉัน เมื่อผม/ดิฉัน พูดข้อความนี้แล้วให้คุณ "+split.get_FirstName(patient_model.getName())+" พูดตาม ผม/ดิฉัน จะบอกเพียงครั้งเดียว “ใครใคร่ขายไข่ไก่”");

        ArrayList<String> get_no7 = database.get_no7(Patient_PK);
        if (get_no7.get(0) != null){

            if (split.check_answer(get_no7.get(0))){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup7_1.getChildAt(0)).setChecked(true);
                edit7_1.setText(split.get_answer(get_no7.get(0)));
                checkradio7_1 = "correct";
            } else {
                ((RadioButton)radioGroup7_1.getChildAt(1)).setChecked(true);
                edit7_1.setText(split.get_answer(get_no7.get(0)));
                checkradio7_1 = "wrong";
            }

//            for (int i = 0; i < radioGroup7_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup7_1.getChildAt(i).setEnabled(false);
//            }

            //edit7_1.setFocusable(false); //ปิดไม่ให้แก้ไขได้

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no7.get(0);
            sumscore = Integer.parseInt(get_no7.get(1)); // get ค่า score


        }

        radioGroup7_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio7_1_correct :
                        checkradio7_1 = "correct";
                        edit7_1.setText("ใครใคร่ขายไข่ไก่");
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio7_1_wrong :
                        if (checkradio7_1.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit7_1.getText().toString().equals("ใครใคร่ขายไข่ไก่")){
                            edit7_1.setText("");
                        }
                        checkradio7_1 = "wrong";
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup7_1.getCheckedRadioButtonId() == -1 || edit7_1.getText().toString().equals("") ) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkradio7_1.equals("correct")) {
                        get_EditText1 = edit7_1.getText() + "_ถูก";
                    } else if (checkradio7_1.equals("wrong")) {
                        get_EditText1 = edit7_1.getText() + "_ผิด";
                    }

                    database.update_no7(Patient_PK,get_EditText1,sumscore);

                    Intent go_no8 = new Intent(getApplicationContext(),No_8.class);
                    startActivity(go_no8);
                    finish();
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_6 = new Intent(getApplicationContext(),No_6.class);
                startActivity(go_6);
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
                dialog_back.dismiss();
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
