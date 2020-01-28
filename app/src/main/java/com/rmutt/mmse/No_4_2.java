package com.rmutt.mmse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;

public class No_4_2 extends AppCompatActivity {

    EditText edit1;
    TextView question;
    Button next,before;
    Dialog dialog_back;
    int sumscore = 0;
    String get_EditText1,getAnser1,getAnser2,getAnser3,getAnser4,getAnser5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_4_2);

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
        final Split split = new Split();

        Patient_Model patient_model = database.patient(patient_ID);

        next = findViewById(R.id.next);
        before = findViewById(R.id.before);
        edit1 = findViewById(R.id.edit1);
        question = findViewById(R.id.question);

        question.setText("ผม/ดิฉัน จะสะกดคำว่า มะนาวให้คุณ "+patient_model.getName()+" ฟัง แล้วคุณ "+patient_model.getName()
        +" สะกดถอยหลังจากพยัญชนะตัวหลังไปตัวแรก คำว่า มะนาว สะกดว่า มอม้า-สระอะ-นอหนู-สระอา-วอแหวน คุณ "+patient_model.getName()+" สะกดถอยหลังให้ฟัง");

        ArrayList<String> get_no4 = database.get_no4(patient_ID);
        if (get_no4.get(0) != null){

            StringBuilder sum_string = new StringBuilder();
            int get_score =get_no4.size() - 1; //เช็คว่าตำแหน่ง score อยู่ที่ตำแหน่งใด

//            for (int i=0;i<get_score;i++){
//                sum_string.append(split.get_answer(get_no4.get(i)));
////                Log.d("sss",String.valueOf(i));
////                Log.d("sss",get_no4.get(i));
//            }

            edit1.setText(get_no4.get(get_score-1));
            edit1.setFocusable(false); // ปิดไม่ได้ผู้ใช้แก้ไข edit text ได้

            //รับค่าเดิมของแต่ละข้อโดยไม่ตัดคำเลย
            getAnser1 = get_no4.get(0);
            getAnser2 = get_no4.get(1);
            getAnser3 = get_no4.get(2);
            getAnser4 = get_no4.get(3);
            getAnser5 = get_no4.get(4);

            sumscore = Integer.parseInt(get_no4.get(get_score)); // get ค่า score

        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                } else {
                    get_EditText1 = edit1.getText().toString();
                    ArrayList<String> segment = split.segmentation(get_EditText1);

                    if (segment.get(0).equals("ว")){
                        getAnser1 = segment.get(0) + "_ถูก";
                        sumscore = sumscore+1;
                    } else {
                        getAnser1 = segment.get(0) + "_ผิด";
                    }

                    if (segment.get(1).equals("า")){
                        getAnser2 = segment.get(1) + "_ถูก";
                        sumscore = sumscore+1;
                    } else {
                        getAnser2 = segment.get(1) + "_ผิด";
                    }

                    if (segment.get(2).equals("น")){
                        getAnser3 = segment.get(2) + "_ถูก";
                        sumscore = sumscore+1;
                    } else {
                        getAnser3 = segment.get(2) + "_ผิด";
                    }

                    if (segment.get(3).equals("ะ")){
                        getAnser4 = segment.get(3) + "_ถูก";
                        sumscore = sumscore+1;
                    } else {
                        getAnser4 = segment.get(3) + "_ผิด";
                    }

                    if (segment.get(4).equals("ม")){
                        getAnser5 = segment.get(4) + "_ถูก";
                        sumscore = sumscore+1;
                    } else {
                        getAnser5 = segment.get(4) + "_ผิด";
                    }

                    database.update_no4(patient_ID,getAnser1,getAnser2,
                            getAnser3,getAnser4,getAnser5,sumscore,get_EditText1);

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
