package com.example.pdfreader3;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainViewHolder extends RecyclerView.ViewHolder {

    public TextView textName;
    public CardView cardView;


    public MainViewHolder(@NonNull View itemView) {
        super(itemView);

        textName = itemView.findViewById(R.id.pdf_textName);
        cardView = itemView.findViewById(R.id.pdf_cardView);

    }
}
