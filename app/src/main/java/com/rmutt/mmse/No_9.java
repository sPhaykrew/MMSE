package com.rmutt.mmse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;

public class No_9 extends AppCompatActivity {

    RadioGroup radioGroup9_1;
    EditText edit9_1;
    Button next,before,show_message;
    Dialog dialog_back,dialog_message;
    int sumscore = 0;

    String checkradio9_1 = "";
    String get_EditText1;

    TextView question;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_9);

        dialog_back = new Dialog(this);
        dialog_message = new Dialog(this);

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
        show_message = findViewById(R.id.show_message);

        radioGroup9_1 = findViewById(R.id.radiogroup9_1);
        edit9_1 = findViewById(R.id.edit9_1);

        question = findViewById(R.id.question);

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String patient_ID = sp.getString("Patient_ID", "null");
        final Database database = new Database(getApplicationContext());

        Patient_Model patient_model = database.patient(patient_ID);

        question.setText("ต่อไปนี้เป็นคำสั่งที่เขียนเป็นตัวหนังสือต้องการให้คุณ "+patient_model.getName()+" อ่านแล้วทำตามโดยจะอ่านออกเสียงหรือในใจก็ได้");

        show_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_show_message = new Intent(getApplicationContext(),No_9_show_message.class);
                startActivity(go_show_message);
            }
        });

        ArrayList<String> get_no9 = database.get_no9(patient_ID);
        if (get_no9.get(0) != null){
            Split split = new Split();

            if (split.check_answer(get_no9.get(0))){ //สั้งให้ radiogroup เช็คคำตอบ
                ((RadioButton) radioGroup9_1.getChildAt(0)).setChecked(true);
                edit9_1.setText(split.get_answer(get_no9.get(0)));
            } else {
                ((RadioButton) radioGroup9_1.getChildAt(1)).setChecked(true);
                edit9_1.setText(split.get_answer(get_no9.get(0)));
            }

//            for (int i = 0; i < radioGroup9_1.getChildCount(); i++) { // สั่งให้ radiogroup เช็คไม่ได้
//                radioGroup9_1.getChildAt(i).setEnabled(false);
//            }

            //edit9_1.setFocusable(false);

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no9.get(0);
            sumscore = Integer.parseInt(get_no9.get(1)); // get ค่า score
        }

        radioGroup9_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio9_1_correct :
                        checkradio9_1 = "correct";
                        edit9_1.setText("หลับตาได้");
                        sumscore = sumscore + 1;
                        break;
                    case R.id.radio9_1_wrong :
                        if (checkradio9_1.equals("correct"))
                        {
                            sumscore = sumscore - 1;
                        }

                        if (edit9_1.getText().toString().equals("หลับตาได้")){
                            edit9_1.setText("");
                        }
                        checkradio9_1 = "wrong";
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup9_1.getCheckedRadioButtonId() == -1 || edit9_1.getText().toString().equals("") ) {
                    Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkradio9_1.equals("correct")) {
                        get_EditText1 = edit9_1.getText() + "_ถูก";
                    } else if (checkradio9_1.equals("wrong")) {
                        get_EditText1 = edit9_1.getText() + "_ผิด";
                    }

                    database.update_no9(patient_ID,get_EditText1,sumscore);

                    Intent go_no10 = new Intent(getApplicationContext(),No_10.class);
                    startActivity(go_no10);
                    finish();

                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_8 = new Intent(getApplicationContext(),No_8.class);
                startActivity(go_8);
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
