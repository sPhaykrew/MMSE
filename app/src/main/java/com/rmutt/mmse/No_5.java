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

import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;

public class No_5 extends AppCompatActivity {

    RadioGroup radioGroup5_1, radioGroup5_2, radioGroup5_3;
    EditText edit5_1, edit5_2, edit5_3;
    TextView textView1,textView2,textView3;
    Button next,before;
    Dialog dialog_back;
    String check_test;
    int sumscore = 0;

    String checkradio5_1 = "";
    String checkradio5_2 = "";
    String checkradio5_3 = "";

    String get_EditText1,get_EditText2,get_EditText3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_5);

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

        radioGroup5_1 = findViewById(R.id.radiogroup3_1);
        radioGroup5_2 = findViewById(R.id.radiogroup3_2);
        radioGroup5_3 = findViewById(R.id.radiogroup3_3);

        edit5_1 = findViewById(R.id.edit3_1);
        edit5_2 = findViewById(R.id.edit3_2);
        edit5_3 = findViewById(R.id.edit3_3);

        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);

        next = findViewById(R.id.next);
        before = findViewById(R.id.before);

        final Patient_Model patient_model = database.patient(patient_ID);
        check_test = patient_model.getCheck_test();

        switch (check_test) {
            case "ใช่" :
                textView1.setText("ต้นไม้");
                textView2.setText("ทะเล");
                textView3.setText("รถยนต์");
                break;
            case "ไม่" :
                textView1.setText("ดอกไม้");
                textView2.setText("แม่น้ำ");
                textView3.setText("รถไฟ");
                break;
        }

        edit5_1.setHint("พิมพ์กรณีผิดของ"+textView1.getText());
        edit5_2.setHint("พิมพ์กรณีผิดของ"+textView2.getText());
        edit5_3.setHint("พิมพ์กรณีผิดของ"+textView3.getText());

//        edit5_1.setFocusable(false); // ปิดไว้ไม่ให้พิมพ์ได้ จะพิมพ์ได้ตัวเมื่อเลือก"ไม่พูด"
//        edit5_2.setFocusable(false);
//        edit5_3.setFocusable(false);

        //ถ้าเคยทำไว้แล้วจะ set ตำคอบไว้กับปิดไม่ให้แก้ไข
        ArrayList<String> get_no5 = database.get_no5(patient_ID);
        if (get_no5.get(0) != null){
            Split split = new Split();

            if (split.check_answer(get_no5.get(0))){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton) radioGroup5_1.getChildAt(0)).setChecked(true);
                edit5_1.setText(split.get_answer(get_no5.get(0)));
            } else {
                ((RadioButton) radioGroup5_1.getChildAt(1)).setChecked(true);
                edit5_1.setText(split.get_answer(get_no5.get(0)));
            }

            if (split.check_answer(get_no5.get(1))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton) radioGroup5_2.getChildAt(0)).setChecked(true);
                edit5_2.setText(split.get_answer(get_no5.get(1)));
            } else {
                ((RadioButton) radioGroup5_2.getChildAt(1)).setChecked(true);
                edit5_2.setText(split.get_answer(get_no5.get(1)));
            }

            if (split.check_answer(get_no5.get(2))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton) radioGroup5_3.getChildAt(0)).setChecked(true);
                edit5_3.setText(split.get_answer(get_no5.get(2)));
            } else {
                ((RadioButton) radioGroup5_3.getChildAt(1)).setChecked(true);
                edit5_3.setText(split.get_answer(get_no5.get(2)));
            }

//            for (int i = 0; i < radioGroup5_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup5_1.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup5_2.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup5_2.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup5_3.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup5_3.getChildAt(i).setEnabled(false);
//            }

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no5.get(0);
            get_EditText2 = get_no5.get(1);
            get_EditText3 = get_no5.get(2);

            sumscore = Integer.parseInt(get_no5.get(3)); // get ค่า score

        }

        radioGroup5_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio3_1_correct :
                        edit5_1.setText(textView1.getText().toString());
                        //edit5_1.setFocusable(false); // ปิดไว้ไม่ให้พิมพ์ได้ จะพิมพ์ได้ตัวเมื่อเลือก"ไม่พูด"
                        checkradio5_1 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio3_1_wrong :
                        //edit5_1.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio5_1.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit5_1.getText().toString().equals(textView1.getText().toString())){
                            edit5_1.setText("");
                        }
                        checkradio5_1 = "wrong";
                        break;
                }
            }
        });

        radioGroup5_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio3_2_correct :
                        edit5_2.setText(textView2.getText().toString());
                        //edit5_2.setFocusable(false); // ปิดไว้ไม่ให้พิมพ์ได้ จะพิมพ์ได้ตัวเมื่อเลือก"ไม่พูด"
                        checkradio5_2 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio3_2_wrong :
                        //edit5_2.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio5_2.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit5_2.getText().toString().equals(textView2.getText().toString())){
                            edit5_2.setText("");
                        }
                        checkradio5_2 = "wrong";
                        break;
                }
            }
        });

        radioGroup5_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio3_3_correct :
                        edit5_3.setText(textView3.getText().toString());
                        //edit5_3.setFocusable(false); // ปิดไว้ไม่ให้พิมพ์ได้ จะพิมพ์ได้ตัวเมื่อเลือก"ไม่พูด"
                        checkradio5_3 = "correct";
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio3_3_wrong :
                        //edit5_3.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio5_3.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit5_3.getText().toString().equals(textView3.getText().toString())){
                            edit5_3.setText("");
                        }
                        checkradio5_3 = "wrong";
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup5_1.getCheckedRadioButtonId() == -1 || radioGroup5_2.getCheckedRadioButtonId() == -1 ||
                        radioGroup5_3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else if (checkradio5_1.equals("wrong") && edit5_1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else if (checkradio5_2.equals("wrong") && edit5_2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else if (checkradio5_3.equals("wrong") && edit5_3.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else {
                    if (checkradio5_1.equals("correct")) {
                        get_EditText1 = textView1.getText() + "_ถูก";
                    } else if (checkradio5_1.equals("wrong")) {
                        get_EditText1 = edit5_1.getText() + "_ผิด";
                    }

                    if (checkradio5_2.equals("correct")) {
                        get_EditText2 = textView2.getText() + "_ถูก";
                    } else if (checkradio5_2.equals("wrong")) {
                        get_EditText2 = edit5_2.getText() + "_ผิด";
                    }

                    if (checkradio5_3.equals("correct")) {
                        get_EditText3 = textView3.getText() + "_ถูก";
                    } else if (checkradio5_3.equals("wrong")) {
                        get_EditText3 = edit5_3.getText() + "_ผิด";
                    }

                    database.update_no5(patient_ID,get_EditText1,get_EditText2,get_EditText3,sumscore);

                    Intent go_no6 = new Intent(getApplicationContext(),No_6.class);
                    startActivity(go_no6);
                    finish();

                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")) {
                    switch (patient_model.getCalculate()) {
                        case "เป็น":
                            Intent go_no4_1 = new Intent(getApplicationContext(), No_4_1.class);
                            startActivity(go_no4_1);
                            finish();
                            break;
                        case "ไม่เป็น":
                            Intent go_no4_2 = new Intent(getApplicationContext(), No_4_2.class);
                            startActivity(go_no4_2);
                            finish();
                            break;
                    }
                } else {
                    Intent go_no3 = new Intent(getApplicationContext(),No_3.class);
                    startActivity(go_no3);
                    finish();
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

}
