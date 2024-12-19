package com.example.pdfreader3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private Context context;
    private List<File> pdfFiles;
    public static File selectedFile = null;

    public MainAdapter(Context context, List<File> pdfFiles) {
        this.context = context;
        this.pdfFiles = pdfFiles;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.textName.setText(pdfFiles.get(position).getName());
        holder.textName.setSelected(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PdfReaderActivity.class);
                selectedFile = pdfFiles.get(holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }
}
