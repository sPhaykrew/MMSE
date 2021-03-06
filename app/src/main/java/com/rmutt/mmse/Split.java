package com.rmutt.mmse;

import java.util.ArrayList;
import java.util.Arrays;

public class Split {

    public boolean check_answer(String answer){
        String[] get_answer = answer.split("_");
        if (get_answer[1].equals("ถูก")){
            return true;
        } else {
            return false;
        }
    }

    public String get_check_answer(String answer){
        String[] get_check_answer = answer.split("_");
        return get_check_answer[1];
    }

    public String get_answer(String answer){
        String[] get_anser = answer.split("_");
        return get_anser[0];
    }

    public ArrayList<String> segmentation(String answer){
        String[] get_anser = answer.split("(?!^)");
        ArrayList<String> segment = new ArrayList<>(Arrays.asList(get_anser));
        return segment;
    }

    public String get_nameImage(String pathImage){
        String[] get_nameImage = pathImage.split("/");
        return get_nameImage[get_nameImage.length-1];
    }

    public String get_FirstName(String FullName){
        String[] get_FistName = FullName.split(" ");
        return get_FistName[0];
    }
}
