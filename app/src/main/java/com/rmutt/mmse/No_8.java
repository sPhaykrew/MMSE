package com.rmutt.mmse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;

public class No_8 extends AppCompatActivity {

    RadioGroup radioGroup8_1,radioGroup8_2,radioGroup8_3;
    EditText edit8_1,edit8_2,edit8_3;
    TextView question,question3;
    Button next,before;
    Dialog dialog_back;

    int sumscore = 0;

    String checkradio8_1 = "";
    String checkradio8_2 = "";
    String checkradio8_3 = "";

    String get_EditText1,get_EditText2,get_EditText3;

    Spinner spinner_place;
    String[] place_list = {"","พื้น","โต๊ะ","เตียง"};

    String check_spinner = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_8);

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

        next = findViewById(R.id.next);
        before = findViewById(R.id.before);

        question = findViewById(R.id.question);
        question3 = findViewById(R.id.question3);

        radioGroup8_1 = findViewById(R.id.radiogroup8_1);
        radioGroup8_2 = findViewById(R.id.radiogroup8_2);
        radioGroup8_3 = findViewById(R.id.radiogroup8_3);

        edit8_1 = findViewById(R.id.edit8_1);
        edit8_2 = findViewById(R.id.edit8_2);
        edit8_3 = findViewById(R.id.edit8_3);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String patient_ID = sp.getString("Patient_ID", "null");
        final Database database = new Database(getApplicationContext());
        final Patient_Model patient_model = database.patient(patient_ID);

        spinner_place = findViewById(R.id.spinner_place);
        ArrayAdapter adapter_place = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,place_list);
        spinner_place.setAdapter(adapter_place);

        question.setText("ฟังดีๆนะเดี๋ยว ผม/ดิฉัน จะส่งกระดาษให้คุณ แล้วคุณ "+patient_model.getName()+" รับด้วยมือขวาพับครึ่งกระดาษ");

//        edit8_1.setFocusable(false); // ปิดไว้ไม่ให้พิมพ์ได้ จะพิมพ์ได้ตัวเมื่อเลือก"ถูก"
//        edit8_2.setFocusable(false);
//        edit8_3.setFocusable(false);

        ArrayList<String> get_no8 = database.get_no8(patient_ID);
        if (get_no8.get(0) != null){
            Split split = new Split();

            if (split.check_answer(get_no8.get(0))){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup8_1.getChildAt(0)).setChecked(true);
                edit8_1.setText(split.get_answer(get_no8.get(0)));
            } else {
                ((RadioButton)radioGroup8_1.getChildAt(1)).setChecked(true);
                edit8_1.setText(split.get_answer(get_no8.get(0)));
            }

            if (split.check_answer(get_no8.get(1))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup8_2.getChildAt(0)).setChecked(true);
                edit8_2.setText(split.get_answer(get_no8.get(1)));
            } else {
                ((RadioButton)radioGroup8_2.getChildAt(1)).setChecked(true);
                edit8_2.setText(split.get_answer(get_no8.get(1)));
            }

            if (split.check_answer(get_no8.get(1))){//สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton)radioGroup8_3.getChildAt(0)).setChecked(true);
                edit8_3.setText(split.get_answer(get_no8.get(2)));
            } else {
                ((RadioButton)radioGroup8_3.getChildAt(1)).setChecked(true);
                edit8_3.setText(split.get_answer(get_no8.get(2)));
            }

//            for (int i = 0; i < radioGroup8_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup8_1.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup8_2.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup8_2.getChildAt(i).setEnabled(false);
//            }
//
//            for (int i = 0; i < radioGroup8_3.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup8_3.getChildAt(i).setEnabled(false);
//            }


            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no8.get(0);
            get_EditText2 = get_no8.get(1);
            get_EditText3 = get_no8.get(2);

            int place_position = 0;

            if (get_no8.get(3) != null) { //หาค่า position ของ season
                switch (split.get_answer(get_no8.get(3))) {
                    case "พื้น":
                        place_position = 1;
                        break;
                    case "โต๊ะ":
                        place_position = 2;
                        break;
                    case "เตียง":
                        place_position = 3;
                        break;
                }
            }
            spinner_place.setSelection(place_position); // set season select
            //spinner_place.setEnabled(false);

            sumscore = Integer.parseInt(get_no8.get(4)); // get ค่า score

        }

        spinner_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                check_spinner = spinner_place.getSelectedItem().toString();
                question3.setText("วางไว้ที่" + spinner_place.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroup8_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio8_1_correct :
                        checkradio8_1 = "correct";
                        edit8_1.setText("รับด้วยมือขวา");
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio8_1_wrong :
//                        edit8_1.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio8_1.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit8_1.getText().toString().equals("รับด้วยมือขวา")){
                            edit8_1.setText("");
                        }
                        checkradio8_1 = "wrong";
                        break;
                }
            }
        });

        radioGroup8_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio8_2_correct :
                        checkradio8_2 = "correct";
                        edit8_2.setText("พับครึ่ง");
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio8_2_wrong :
//                        edit8_2.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio8_2.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit8_2.getText().toString().equals("พับครึ่ง")){
                            edit8_2.setText("");
                        }
                        checkradio8_2 = "wrong";
                        break;
                }
            }
        });

        radioGroup8_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio8_3_correct :
                        checkradio8_3 = "correct";
                        edit8_3.setText(question3.getText().toString());
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio8_3_wrong :
//                        edit8_3.setFocusableInTouchMode(true); //เปิดให้แก้ไข edittext
                        if (checkradio8_3.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit8_3.getText().toString().equals(question3.getText().toString())){
                            edit8_3.setText("");
                        }
                        checkradio8_3 = "wrong";
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup8_1.getCheckedRadioButtonId() == -1 || radioGroup8_2.getCheckedRadioButtonId() == -1 ||
                radioGroup8_3.getCheckedRadioButtonId() == -1 || check_spinner.equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else if (checkradio8_1.equals("wrong") && edit8_1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else if (checkradio8_2.equals("wrong") && edit8_2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else if (checkradio8_3.equals("wrong") && edit8_3.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkradio8_1.equals("correct")) {
                        get_EditText1 = edit8_1.getText() + "_ถูก";
                    } else if (checkradio8_1.equals("wrong")) {
                        get_EditText1 = edit8_1.getText() + "_ผิด";
                    }

                    if (checkradio8_2.equals("correct")) {
                        get_EditText2 = edit8_2.getText() + "_ถูก";
                    } else if (checkradio8_2.equals("wrong")) {
                        get_EditText2 = edit8_2.getText() + "_ผิด";
                    }

                    if (checkradio8_3.equals("correct")) {
                        get_EditText3 = edit8_3.getText() + "_ถูก";
                    } else if (checkradio8_3.equals("wrong")) {
                        get_EditText3 = edit8_3.getText() + "_ผิด";
                    }

                    database.update_no8(patient_ID,get_EditText1,get_EditText2,get_EditText3,spinner_place.getSelectedItem().toString(),sumscore);

                    if (!patient_model.getEducation().equals("ไม่ได้เรียนหนังสือ")) {
                        Intent go_no9 = new Intent(getApplicationContext(), No_9.class);
                        startActivity(go_no9);
                        finish();
                    } else {
                        Intent go_no11 = new Intent(getApplicationContext(), No_11.class);
                        startActivity(go_no11);
                        finish();
                    }
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_no7 = new Intent(getApplicationContext(),No_7.class);
                startActivity(go_no7);
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
