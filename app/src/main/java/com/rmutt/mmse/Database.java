package com.rmutt.mmse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;

public class Database extends SQLiteAssetHelper {

    public Database(Context context){
        super(context,"MMSE.db",null,1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    public void insert_patient_id_test(String patient_PK){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("Patient_PK",patient_PK);
        db.insert("Test",null, Val);
        db.close();
    }

    public ArrayList<Patient_Model> Patient_all(){ //query all patient
        ArrayList<Patient_Model> patient_models = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select patient_ID,name,age,education,calculate,check_test,status,where_,time,patient_PK " +
                "from patient",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Patient_Model model = new Patient_Model();
            model.setPatient_ID(cursor.getString(0));
            model.setName(cursor.getString(1));
            model.setAge(cursor.getInt(2));
            model.setEducation(cursor.getString(3));
            model.setCalculate(cursor.getString(4));
            model.setCheck_test(cursor.getString(5));
            model.setStatus(cursor.getString(6));
            model.setWhere(cursor.getString(7));
            model.setTime(cursor.getString(8));
            model.setPatient_PK(cursor.getString(9));

            switch (model.getStatus()){
                case "เริ่มทำ" : model.setStatus_color(Color.parseColor("#EB5757")); break;
                case "ทำต่อ" : model.setStatus_color(Color.parseColor("#F2EB4C")); break;
                case "พร้อมส่ง" : model.setStatus_color(Color.parseColor("#27AE60")); break;
            }
            patient_models.add(model);
            cursor.moveToNext();
        }
        db.close();
        return patient_models;
    }

    public void delete_patient (String patient_PK){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("test","patient_PK = '" + patient_PK +"'",null);
        db.delete("patient","patient_PK = '" + patient_PK +"'",null);
        db.close();
    }

    public Patient_Model patient(String patient_PK){
        Patient_Model model = new Patient_Model();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select patient_ID,name,age,education,calculate,check_test,status,where_,time,Patient_PK " +
                "from patient where patient_PK = '"+patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            model.setPatient_ID(cursor.getString(0));
            model.setName(cursor.getString(1));
            model.setAge(cursor.getInt(2));
            model.setEducation(cursor.getString(3));
            model.setCalculate(cursor.getString(4));
            model.setCheck_test(cursor.getString(5));
            model.setStatus(cursor.getString(6));
            model.setWhere(cursor.getString(7));
            model.setTime(cursor.getString(8));
            model.setPatient_PK(cursor.getString(9));

            switch (model.getStatus()){
                case "เริ่มทำ" : model.setStatus_color(Color.parseColor("#EB5757")); break;
                case "ทำต่อ" : model.setStatus_color(Color.parseColor("#F2EB4C")); break;
                case "พร้อมส่ง" : model.setStatus_color(Color.parseColor("#27AE60")); break;
            }
            cursor.moveToNext();
        }
        db.close();
        return model;
    }

    public void update_patient (String patient_PK,String name,int age,String education,String calculate,String check_test
    ,String where,String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("name",name);
        Val.put("age",age);
        Val.put("education",education);
        Val.put("calculate",calculate);
        Val.put("check_test",check_test);
        Val.put("where_",where);
        Val.put("time",time);
        db.update("Patient",Val, "patient_PK='" + patient_PK +"'", null);
        db.close();
    }

    public void update_patient_status (String patient_PK,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("status",status);
        db.update("Patient",Val, "patient_PK='" + patient_PK +"'", null);
        db.close();
    }

    public void insert_patient (String patient_ID,String name,int age,String education,String calculate,String check_test
    ,String where,String time,String status,String patient_PK_auto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("Patient_PK",patient_PK_auto);
        Val.put("patient_ID",patient_ID);
        Val.put("name",name);
        Val.put("age",age);
        Val.put("education",education);
        Val.put("calculate",calculate);
        Val.put("check_test",check_test);
        Val.put("status",status);
        Val.put("where_",where);
        Val.put("time",time);
        db.insert("Patient", null, Val);
        db.close();
    }

    public void update_no1(String patient_PK,String no1_1,String no1_2,String no1_3,String no1_4,String no1_5,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no1_1",no1_1);
        Val.put("no1_2",no1_2);
        Val.put("no1_3",no1_3);
        Val.put("no1_4",no1_4);
        Val.put("no1_5",no1_5);
        Val.put("sum_score1",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK +"'",null);
        db.close();
    }

    public ArrayList<String> get_no1(String patient_PK){
        ArrayList<String> get_no1 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no1_1,no1_2,no1_3,no1_4,no1_5,sum_score1 from Test where Patient_PK = '" + patient_PK +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no1.add(cursor.getString(0));
            get_no1.add(cursor.getString(1));
            get_no1.add(cursor.getString(2));
            get_no1.add(cursor.getString(3));
            get_no1.add(cursor.getString(4));
            get_no1.add(String.valueOf(cursor.getInt(5)));
            cursor.moveToNext();
        }
        db.close();
        return get_no1;
    }

    public void update_no2(String patient_PK,String no2_1,String no2_2,String no2_3,String no2_4,String no2_5,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no2_1",no2_1);
        Val.put("no2_2",no2_2);
        Val.put("no2_3",no2_3);
        Val.put("no2_4",no2_4);
        Val.put("no2_5",no2_5);
        Val.put("sum_score2",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK +"'",null);
        db.close();
    }

    public ArrayList<String> get_no2(String patient_PK){
        ArrayList<String> get_no2 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no2_1,no2_2,no2_3,no2_4,no2_5,sum_score2 from Test where patient_PK = '" + patient_PK +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no2.add(cursor.getString(0));
            get_no2.add(cursor.getString(1));
            get_no2.add(cursor.getString(2));
            get_no2.add(cursor.getString(3));
            get_no2.add(cursor.getString(4));
            get_no2.add(String.valueOf(cursor.getInt(5)));
            cursor.moveToNext();
        }
        db.close();
        return get_no2;
    }

    public void update_no3(String patient_PK,String no3_1,String no3_2,String no3_3,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no3_1",no3_1);
        Val.put("no3_2",no3_2);
        Val.put("no3_3",no3_3);
        Val.put("sum_score3",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public ArrayList<String> get_no3(String patient_PK){
        ArrayList<String> get_no3 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no3_1,no3_2,no3_3,sum_score3 from Test where patient_PK = '" + patient_PK +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no3.add(cursor.getString(0));
            get_no3.add(cursor.getString(1));
            get_no3.add(cursor.getString(2));
            get_no3.add(String.valueOf(cursor.getInt(3)));
            cursor.moveToNext();
        }
        db.close();
        return get_no3;
    }

    public void update_no4(String patient_PK,String no4_1,String no4_2,String no4_3,String no4_4,String no4_5,int sumscore,String sumstring){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no4_1",no4_1);
        Val.put("no4_2",no4_2);
        Val.put("no4_3",no4_3);
        Val.put("no4_4",no4_4);
        Val.put("no4_5",no4_5);
        Val.put("sum_score4",sumscore);
        Val.put("sum_string_4_2",sumstring);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public ArrayList<String> get_no4(String patient_PK){
        ArrayList<String> get_no4 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no4_1,no4_2,no4_3,no4_4,no4_5,sum_string_4_2,sum_score4 from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no4.add(cursor.getString(0));
            get_no4.add(cursor.getString(1));
            get_no4.add(cursor.getString(2));
            get_no4.add(cursor.getString(3));
            get_no4.add(cursor.getString(4));
            get_no4.add(cursor.getString(5));
            get_no4.add(String.valueOf(cursor.getInt(6)));
            cursor.moveToNext();
        }
        db.close();
        return get_no4;
    }

    public void update_no5(String patient_PK,String no5_1,String no5_2,String no5_3,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no5_1",no5_1);
        Val.put("no5_2",no5_2);
        Val.put("no5_3",no5_3);
        Val.put("sum_score5",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK +"'",null);
        db.close();
    }

    public ArrayList<String> get_no5(String patient_PK){
        ArrayList<String> get_no5 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no5_1,no5_2,no5_3,sum_score5 from Test where patient_PK = '" + patient_PK +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no5.add(cursor.getString(0));
            get_no5.add(cursor.getString(1));
            get_no5.add(cursor.getString(2));
            get_no5.add(String.valueOf(cursor.getInt(3)));
            cursor.moveToNext();
        }
        db.close();
        return get_no5;
    }

    public void update_no6(String patient_PK,String no6_1,String no6_2,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no6_1",no6_1);
        Val.put("no6_2",no6_2);
        Val.put("sum_score6",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK +"'",null);
        db.close();
    }

    public ArrayList<String> get_no6(String patient_PK){
        ArrayList<String> get_no6 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no6_1,no6_2,sum_score6 from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no6.add(cursor.getString(0));
            get_no6.add(cursor.getString(1));
            get_no6.add(String.valueOf(cursor.getInt(2)));
            cursor.moveToNext();
        }
        db.close();
        return get_no6;
    }

    public void update_no7(String patient_PK,String no7_1,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no7_1",no7_1);
        Val.put("sum_score7",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK +"'",null);
        db.close();
    }

    public ArrayList<String> get_no7(String patient_PK){
        ArrayList<String> get_no7 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no7_1,sum_score7 from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no7.add(cursor.getString(0));
            get_no7.add(String.valueOf(cursor.getInt(1)));
            cursor.moveToNext();
        }
        db.close();
        return get_no7;
    }

    public void update_no8(String patient_PK,String no8_1,String no8_2,String no8_3,String place8,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no8_1",no8_1);
        Val.put("no8_2",no8_2);
        Val.put("no8_3",no8_3);
        Val.put("place8",place8);
        Val.put("sum_score8",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public ArrayList<String> get_no8(String patient_PK){
        ArrayList<String> get_no8 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no8_1,no8_2,no8_3,place8,sum_score8 from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no8.add(cursor.getString(0));
            get_no8.add(cursor.getString(1));
            get_no8.add(cursor.getString(2));
            get_no8.add(cursor.getString(3));
            get_no8.add(String.valueOf(cursor.getInt(4)));
            cursor.moveToNext();
        }
        db.close();
        return get_no8;
    }

    public void update_no9(String patient_PK,String no9_1,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no9_1",no9_1);
        Val.put("sum_score9",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public ArrayList<String> get_no9(String patient_PK){
        ArrayList<String> get_no9 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no9_1,sum_score9 from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no9.add(cursor.getString(0));
            get_no9.add(String.valueOf(cursor.getInt(1)));
            cursor.moveToNext();
        }
        db.close();
        return get_no9;
    }

    public void update_no10(String patient_PK,String no10_1,String path_picture,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no10_1",no10_1);
        Val.put("no10_path_picture",path_picture);
        Val.put("sum_score10",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public ArrayList<String> get_no10(String patient_PK){
        ArrayList<String> get_no11 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no10_1,no10_path_picture,sum_score10 from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no11.add(cursor.getString(0));
            get_no11.add(cursor.getString(1));
            get_no11.add(String.valueOf(cursor.getInt(2)));
            cursor.moveToNext();
        }
        db.close();
        return get_no11;
    }

    public void update_no11(String patient_PK,String no11_1,String path_picture,int sumscore){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("no11_1",no11_1);
        Val.put("no11_path_picture",path_picture);
        Val.put("sum_score11",sumscore);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public ArrayList<String> get_no11(String patient_PK){
        ArrayList<String> get_no11 = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select no11_1,no11_path_picture,sum_score11 from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            get_no11.add(cursor.getString(0));
            get_no11.add(cursor.getString(1));
            get_no11.add(String.valueOf(cursor.getInt(2)));
            cursor.moveToNext();
        }
        db.close();
        return get_no11;
    }

    public void result_score(int score,String patient_PK){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("result_score",score);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public int get_result_score(String patient_PK){
        int result_score = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select result_score from Test where patient_PK = '" + patient_PK +"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            result_score = cursor.getInt(0);
            cursor.moveToNext();
        }
        db.close();
        return result_score;
    }

    public String test_ID(String patient_PK){
        String test_ID = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select test_ID from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            test_ID = cursor.getString(0);
            cursor.moveToNext();
        }
        db.close();
        return test_ID;
    }

    public void update_patient_data_path(String patient_PK,String patient_data_path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("patient_data_path",patient_data_path);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public void update_test_data_path(String patient_PK,String test_data_path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Val = new ContentValues();
        Val.put("test_data_path",test_data_path);
        db.update("Test",Val,"patient_PK='" + patient_PK+"'",null);
        db.close();
    }

    public String get_patient_data_path(String patient_PK){
        String patient_data_path = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select patient_data_path from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            patient_data_path = cursor.getString(0);
            cursor.moveToNext();
        }
        db.close();
        return patient_data_path;
    }

    public String get_test_data_path(String patient_PK){
        String test_data_path = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select test_data_path from Test where patient_PK = '" + patient_PK+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            test_data_path = cursor.getString(0);
            cursor.moveToNext();
        }
        db.close();
        return test_data_path;
    }

}
