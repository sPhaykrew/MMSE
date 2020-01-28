package com.rmutt.mmse.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rmutt.mmse.Login;
import com.rmutt.mmse.R;
import com.rmutt.mmse.history;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<Recycler_ViewHolder>
{

    Context context;
    ArrayList<Patient_Model> model;

    public RecyclerViewAdapter(Context context, ArrayList<Patient_Model> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public Recycler_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);

        return new Recycler_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_ViewHolder holder, int position) {

        holder.patient_ID.setText(model.get(position).getPatient_ID()); //set data
        holder.status.setText(model.get(position).getStatus());
        holder.time.setText(model.get(position).getTime());
        holder.status_corlor.setBackgroundColor(model.get(position).getStatus_color());

        holder.setItemClickListener(new RecyclerClick() {
            @Override
            public void onItemClickListener(View v, int position) {

                String patient_ID = model.get(position).getPatient_ID(); // get ID when click recyclerview

                SharedPreferences sp = context.getSharedPreferences("Patient", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.clear(); // clear data
                editor.apply();

                editor.putString("Patient_ID",patient_ID);
                editor.commit();

                Intent intent = new Intent(context,history.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}