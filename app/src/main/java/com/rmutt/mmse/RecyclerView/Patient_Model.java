package com.rmutt.mmse.RecyclerView;

public class Patient_Model {

    private String patient_ID,status,time,name,education,calculate,check_test,where;
    private int age,status_color;

    public String getPatient_ID() {
        return patient_ID;
    }

    public void setPatient_ID(String patient_ID) {
        this.patient_ID = patient_ID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus_color() {
        return status_color;
    }

    public void setStatus_color(int status_color) {
        this.status_color = status_color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCalculate() {
        return calculate;
    }

    public void setCalculate(String calculate) {
        this.calculate = calculate;
    }

    public String getCheck_test() {
        return check_test;
    }

    public void setCheck_test(String check_test) {
        this.check_test = check_test;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}
