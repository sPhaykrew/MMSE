package com.rmutt.mmse;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rmutt.mmse.RecyclerView.RecyclerViewAdapter;
import com.rmutt.mmse.RecyclerView.Patient_Model;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Patient_Data extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    public Patient_Data() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient__data, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void onResume() { //ทำงานทุกครั่งที่ fragment แปะที่หน้า activity
        super.onResume();

        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),prepare_data());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private ArrayList<Patient_Model> prepare_data(){
        Database database = new Database(getContext());
        ArrayList<Patient_Model> model;
        model = database.Patient_all();
        return model;
    }

}
