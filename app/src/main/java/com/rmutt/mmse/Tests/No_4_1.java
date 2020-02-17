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

public class No_4_1 extends AppCompatActivity {

    EditText edit4_1,edit4_2,edit4_3,edit4_4,edit4_5;
    Button next,before;
    Dialog dialog_back;
    int sumscore = 0;
    String get_EditText1,get_EditText2,get_EditText3,get_EditText4,get_EditText5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_4_1);

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

        SharedPreferences sp = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final String Patient_PK = sp.getString("Patient_PK", "null");
        final Database database = new Database(getApplicationContext());

        edit4_1 = findViewById(R.id.edit1);
        edit4_2 = findViewById(R.id.edit2);
        edit4_3 = findViewById(R.id.edit3);
        edit4_4 = findViewById(R.id.edit4);
        edit4_5 = findViewById(R.id.edit5);

        next = findViewById(R.id.next);
        before = findViewById(R.id.before);

        ArrayList<String> get_no4 = database.get_no4(Patient_PK);
        if (get_no4.get(0) != null){
            Split split = new Split();

            edit4_1.setText(split.get_answer(get_no4.get(0)));
            //edit4_1.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit4_2.setText(split.get_answer(get_no4.get(1)));
            //edit4_2.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit4_3.setText(split.get_answer(get_no4.get(2)));
            //edit4_3.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit4_4.setText(split.get_answer(get_no4.get(3)));
            //edit4_4.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            edit4_5.setText(split.get_answer(get_no4.get(3)));
            //edit4_5.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            get_EditText1 = get_no4.get(0);
            get_EditText2 = get_no4.get(1);
            get_EditText3 = get_no4.get(2);
            get_EditText4 = get_no4.get(3);
            get_EditText5 = get_no4.get(4);

            sumscore = Integer.parseInt(get_no4.get(6)); // get ค่า score

        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit4_1.getText().toString().equals("") || edit4_2.getText().toString().equals("") || edit4_3.getText().toString().equals("") ||
                        edit4_4.getText().toString().equals("") || edit4_5.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else {
                    get_EditText1 = edit4_1.getText().toString();
                    get_EditText2 = edit4_2.getText().toString();
                    get_EditText3 = edit4_3.getText().toString();
                    get_EditText4 = edit4_4.getText().toString();
                    get_EditText5 = edit4_5.getText().toString();

                    if (get_EditText1.equals("93")){
                        get_EditText1 = get_EditText1 + "_ถูก";
                        sumscore = sumscore +1;
                    } else {
                        get_EditText1 = get_EditText1 + "_ผิด";
                    }

                    if (get_EditText2.equals("86")){
                        get_EditText2 = get_EditText2 + "_ถูก";
                        sumscore = sumscore +1;
                    } else {
                        get_EditText2 = get_EditText2 + "_ผิด";
                    }

                    if (get_EditText3.equals("79")){
                        get_EditText3 = get_EditText3 + "_ถูก";
                        sumscore = sumscore +1;
                    } else {
                        get_EditText3 = get_EditText3 + "_ผิด";
                    }

                    if (get_EditText4.equals("72")){
                        get_EditText4 = get_EditText4 + "_ถูก";
                        sumscore = sumscore +1;
                    } else {
                        get_EditText4 = get_EditText4 + "_ผิด";
                    }

                    if (get_EditText5.equals("65")){
                        get_EditText5 = get_EditText5 + "_ถูก";
                        sumscore = sumscore +1;
                    } else {
                        get_EditText5 = get_EditText5 + "_ผิด";
                    }

                    database.update_no4(Patient_PK,get_EditText1,get_EditText2,get_EditText3,get_EditText4,get_EditText5,sumscore,null);

                    Intent go_no5 = new Intent(getApplicationContext(),No_5.class);
                    startActivity(go_no5);
                    finish();
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_no3 = new Intent(getApplicationContext(),No_3.class);
                startActivity(go_no3);
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
