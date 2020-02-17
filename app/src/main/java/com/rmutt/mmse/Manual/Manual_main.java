package com.rmutt.mmse.Manual;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rmutt.mmse.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Manual_main extends Fragment {

    LinearLayout contents1,contents2,contents3,contents4,contents5,contents6,contents7,contents8,contents9;


    public Manual_main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual, container, false);

        contents1 = view.findViewById(R.id.contents1);
        contents1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content1 = new Intent(getContext(),Contents1.class);
                startActivity(content1);
            }
        });

        contents2 = view.findViewById(R.id.contents2);
        contents2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content2 = new Intent(getContext(),Contents2.class);
                startActivity(content2);
            }
        });

        contents3 = view.findViewById(R.id.contents3);
        contents3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content3 = new Intent(getContext(),Contents3.class);
                startActivity(content3);
            }
        });

        contents4 = view.findViewById(R.id.contents4);
        contents4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content4 = new Intent(getContext(),Contents4.class);
                startActivity(content4);
            }
        });

        contents5 = view.findViewById(R.id.contents5);
        contents5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content5 = new Intent(getContext(),Contents5.class);
                startActivity(content5);
            }
        });

        contents6 = view.findViewById(R.id.contents6);
        contents6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content6 = new Intent(getContext(),Contents6.class);
                startActivity(content6);
            }
        });

        contents7 = view.findViewById(R.id.contents7);
        contents7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content7 = new Intent(getContext(),Contents7.class);
                startActivity(content7);
            }
        });

        contents8 = view.findViewById(R.id.contents8);
        contents8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content8 = new Intent(getContext(),Contents8.class);
                startActivity(content8);
            }
        });

        contents9 = view.findViewById(R.id.contents9);
        contents9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content9 = new Intent(getContext(),Contents9.class);
                startActivity(content9);
            }
        });

        return view;
    }

}
