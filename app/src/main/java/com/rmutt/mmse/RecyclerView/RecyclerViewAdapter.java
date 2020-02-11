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

import com.rmutt.mmse.Database;
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
    public void onBindViewHolder(@NonNull final Recycler_ViewHolder holder, final int position) {

        final Database database = new Database(context);

        holder.patient_ID.setText(model.get(position).getPatient_ID()); //set data
        holder.status.setText(model.get(position).getStatus());
        holder.time.setText(model.get(position).getTime());
        holder.status_corlor.setBackgroundColor(model.get(position).getStatus_color());

        if (model.get(position).getStatus().equals("พร้อมส่ง")){ //hide button dele if status = พร้อมส่ง
            holder.imageButton.setVisibility(View.INVISIBLE);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete_patient(model.get(position).getPatient_PK());
                removeAt(position);
//                Toast.makeText(context,model.get(position).getPatient_PK(),Toast.LENGTH_SHORT).show();
            }
        });

        holder.setItemClickListener(new RecyclerClick() {
            @Override
            public void onItemClickListener(View v, int position) {

                String Patient_PK = model.get(position).getPatient_PK(); // get ID when click recyclerview

                SharedPreferences sp = context.getSharedPreferences("Patient", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.clear(); // clear data
                editor.apply();

//                Toast.makeText(context,Patient_PK,Toast.LENGTH_SHORT).show();

                editor.putString("Patient_PK",Patient_PK);
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

    public void removeAt(int position) {
        model.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, model.size());
    }
}