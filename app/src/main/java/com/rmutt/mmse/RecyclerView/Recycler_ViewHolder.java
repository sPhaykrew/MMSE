package com.rmutt.mmse.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rmutt.mmse.R;

public class Recycler_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView patient_ID,status,time;
    LinearLayout status_corlor;
    CardView cardView;
    RecyclerClick itemClick;
    ImageButton imageButton;

    Recycler_ViewHolder(@NonNull View itemView) {
        super(itemView);

        patient_ID = itemView.findViewById(R.id.patient_ID);
        status = itemView.findViewById(R.id.status);
        time = itemView.findViewById(R.id.time);
        status_corlor = itemView.findViewById(R.id.color_status);
        cardView = itemView.findViewById(R.id.card_recycler);
        imageButton = itemView.findViewById(R.id.delete);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.itemClick.onItemClickListener(v,getLayoutPosition());
    }

    public void setItemClickListener(RecyclerClick Click){
        this.itemClick = Click;
    }
}
