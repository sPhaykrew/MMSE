package com.rmutt.mmse.Export_Import;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.rmutt.mmse.Database;
import com.rmutt.mmse.RecyclerView.Patient_Model;
import com.rmutt.mmse.Split;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Import_Export {

    Database database;
    static Context context;

    public Import_Export(Context context){
        this.context = context;
    }

    public void import_csv(Uri uri,String PK_auto){

        database = new Database(context);

            try {
                // ContentValues cv = new ContentValues();
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                InputStreamReader isr = new InputStreamReader(inputStream);
                CSVReader dataRead = new CSVReader(isr);

                String[] get_CSV;
                dataRead.readNext(); //ข้าม topic ไป คือข้ามบรรทัดแรก
                while ((get_CSV = dataRead.readNext()) != null) { //get data from csv and insert in db
                    String pk_auto_sum = get_CSV[0]+"_"+PK_auto;
                    database.insert_patient(get_CSV[0],get_CSV[1],Integer.parseInt(get_CSV[2]),null,null,null
                    ,null,null,"เริ่มทำ",pk_auto_sum);
                    database.insert_patient_id_test(pk_auto_sum); //เพิ่ม id ที่หน้า table test ไม่งั้นจะ error
                }
            } catch (Exception e) {
                Log.d("error",e.toString());
            }

        }

    public void export_patient_data(String patient_PK, String mmse_ID){

        Patient_Model patientModel = database.patient(patient_PK);
        String test_ID = database.test_ID(patient_PK);
        int get_result_score = database.get_result_score(patient_PK);

        StringBuilder data = new StringBuilder();
        data.append("รหัสการทำแบบทดสอบ,รหัสอาสาสมัคร,รหัสผู้ป่วย,วันที่ทำแบบทดสอบ,ระดับการศึกษา,สถานที่ทำแบบทดสอบ,ทำแบบทดสอบภายใน 2 เดือนหรือไม่,ผลประเมิน\n");
        data.append(test_ID+","+mmse_ID+","+patientModel.getPatient_ID()+","+patientModel.getTime()+","+patientModel.getEducation()+","+
                patientModel.getWhere()+","+patientModel.getCheck_test()+","+get_result_score);

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MMSE/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdir();
            file.canExecute();
        }
        try {
//            String file_name = directory_path + test_ID +"_" +patientModel.getTime() + "_1" + ".csv";
            String file_name = directory_path + test_ID +"_" + "ข้อมูลผู้ป่วย" + ".csv";
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file_name));
            fileOutputStream.write(data.toString().getBytes());
            fileOutputStream.close();
            database.update_patient_data_path(patient_PK,file_name);
            //Toast.makeText(context, "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }

    public void export_test_data(String patient_PK){

        database = new Database(context);
        Patient_Model patientModel = database.patient(patient_PK);

        Split split = new Split();

        String test_ID = database.test_ID(patient_PK);
        ArrayList no_1 = database.get_no1(patient_PK);
        ArrayList no_2 = database.get_no2(patient_PK);
        ArrayList no_3 = database.get_no3(patient_PK);
        ArrayList no_4 = database.get_no4(patient_PK);
        ArrayList no_5 = database.get_no5(patient_PK);
        ArrayList no_6 = database.get_no6(patient_PK);
        ArrayList no_7 = database.get_no7(patient_PK);
        ArrayList no_8 = database.get_no8(patient_PK);
        ArrayList no_9 = database.get_no9(patient_PK);
        ArrayList no_10 = database.get_no10(patient_PK);
        ArrayList no_11 = database.get_no11(patient_PK);

        StringBuilder data = new StringBuilder();
        data.append("รหัสการทำแบบทดสอบ,เลขคำถาม,เลขข้อ,ผล,คำตอบ\n");

        //no1
        data.append(test_ID+",1,1.1,"+split.get_check_answer(String.valueOf(no_1.get(0)))+","+split.get_answer(String.valueOf(no_1.get(0)))+"\n");
        data.append(test_ID+",1,1.2,"+split.get_check_answer(String.valueOf(no_1.get(1)))+","+split.get_answer(String.valueOf(no_1.get(1)))+"\n");
        data.append(test_ID+",1,1.3,"+split.get_check_answer(String.valueOf(no_1.get(2)))+","+split.get_answer(String.valueOf(no_1.get(2)))+"\n");
        data.append(test_ID+",1,1.4,"+split.get_check_answer(String.valueOf(no_1.get(3)))+","+split.get_answer(String.valueOf(no_1.get(3)))+"\n");
        data.append(test_ID+",1,1.5,"+split.get_check_answer(String.valueOf(no_1.get(4)))+","+split.get_answer(String.valueOf(no_1.get(4)))+"\n");

        //no2
        data.append(test_ID+",2,2.1,"+split.get_check_answer(String.valueOf(no_2.get(0)))+","+split.get_answer(String.valueOf(no_2.get(0)))+"\n");
        data.append(test_ID+",2,2.2,"+split.get_check_answer(String.valueOf(no_2.get(1)))+","+split.get_answer(String.valueOf(no_2.get(1)))+"\n");
        data.append(test_ID+",2,2.3,"+split.get_check_answer(String.valueOf(no_2.get(2)))+","+split.get_answer(String.valueOf(no_2.get(2)))+"\n");
        data.append(test_ID+",2,2.4,"+split.get_check_answer(String.valueOf(no_2.get(3)))+","+split.get_answer(String.valueOf(no_2.get(3)))+"\n");
        data.append(test_ID+",2,2.5,"+split.get_check_answer(String.valueOf(no_2.get(4)))+","+split.get_answer(String.valueOf(no_2.get(4)))+"\n");

        //no3
        data.append(test_ID+",3,3.1,"+split.get_check_answer(String.valueOf(no_3.get(0)))+","+split.get_answer(String.valueOf(no_3.get(0)))+"\n");
        data.append(test_ID+",3,3.2,"+split.get_check_answer(String.valueOf(no_3.get(1)))+","+split.get_answer(String.valueOf(no_3.get(1)))+"\n");
        data.append(test_ID+",3,3.3,"+split.get_check_answer(String.valueOf(no_3.get(2)))+","+split.get_answer(String.valueOf(no_3.get(2)))+"\n");

        //no4
        data.append(test_ID+",4,4.1,"+split.get_check_answer(String.valueOf(no_4.get(0)))+","+split.get_answer(String.valueOf(no_4.get(0)))+"\n");
        data.append(test_ID+",4,4.2,"+split.get_check_answer(String.valueOf(no_4.get(1)))+","+split.get_answer(String.valueOf(no_4.get(1)))+"\n");
        data.append(test_ID+",4,4.3,"+split.get_check_answer(String.valueOf(no_4.get(2)))+","+split.get_answer(String.valueOf(no_4.get(2)))+"\n");
        data.append(test_ID+",4,4.4,"+split.get_check_answer(String.valueOf(no_4.get(3)))+","+split.get_answer(String.valueOf(no_4.get(3)))+"\n");
        data.append(test_ID+",4,4.5,"+split.get_check_answer(String.valueOf(no_4.get(4)))+","+split.get_answer(String.valueOf(no_4.get(4)))+"\n");

        //no5
        data.append(test_ID+",5,5.1,"+split.get_check_answer(String.valueOf(no_5.get(0)))+","+split.get_answer(String.valueOf(no_5.get(0)))+"\n");
        data.append(test_ID+",5,5.2,"+split.get_check_answer(String.valueOf(no_5.get(1)))+","+split.get_answer(String.valueOf(no_5.get(1)))+"\n");
        data.append(test_ID+",5,5.3,"+split.get_check_answer(String.valueOf(no_5.get(2)))+","+split.get_answer(String.valueOf(no_5.get(2)))+"\n");

        //no6
        data.append(test_ID+",6,6.1,"+split.get_check_answer(String.valueOf(no_6.get(0)))+","+split.get_answer(String.valueOf(no_6.get(0)))+"\n");
        data.append(test_ID+",6,6.2,"+split.get_check_answer(String.valueOf(no_6.get(1)))+","+split.get_answer(String.valueOf(no_6.get(1)))+"\n");

        //no7
        data.append(test_ID+",7,7.1,"+split.get_check_answer(String.valueOf(no_7.get(0)))+","+split.get_answer(String.valueOf(no_7.get(0)))+"\n");

        //no8
        data.append(test_ID+",8,8.1,"+split.get_check_answer(String.valueOf(no_8.get(0)))+","+split.get_answer(String.valueOf(no_8.get(0)))+"\n");
        data.append(test_ID+",8,8.2,"+split.get_check_answer(String.valueOf(no_8.get(1)))+","+split.get_answer(String.valueOf(no_8.get(1)))+"\n");
        data.append(test_ID+",8,8.3,"+split.get_check_answer(String.valueOf(no_8.get(2)))+","+split.get_answer(String.valueOf(no_8.get(2)))+"\n");

        //no9
        data.append(test_ID+",9,9.1,"+split.get_check_answer(String.valueOf(no_9.get(0)))+","+split.get_answer(String.valueOf(no_9.get(0)))+"\n");

        //no10
        data.append(test_ID+",10,10.1,"+no_10.get(0)+","+split.get_nameImage(String.valueOf(no_10.get(1)))+"\n");

        //no11
        data.append(test_ID+",11,11.1,"+no_11.get(0)+","+split.get_nameImage(String.valueOf(no_11.get(1)))+"\n");

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/MMSE/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdir();
            file.canExecute();
        }
        try {
            //String file_name = directory_path + test_ID +"_" +patientModel.getTime() + "_2" + ".csv";
            String file_name = directory_path + test_ID +"_" + "ผลการทำแบบทดสอบ" + ".csv";
//            FileOutputStream fileOutputStream = new FileOutputStream(new File(file_name));
//            fileOutputStream.write(data.toString().getBytes());
//            fileOutputStream.close();

            //Writer export = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_name), "Windows-874"));
            Writer export = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_name), StandardCharsets.UTF_8));
            export.write(data.toString());
            export.close();


            database.update_test_data_path(patient_PK,file_name);
            //Toast.makeText(context, "นำออกข้อมูลเสร็จสิ้น", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }
}
