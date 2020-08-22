package com.example.recyclerdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<TaskModel> model;
//    private ArrayList t_id, t_name, t_date, t_time, t_detail;
//
//    CustomAdapter(Context context, ArrayList t_id, ArrayList t_name, ArrayList t_date, ArrayList t_time, ArrayList t_detail){
//        this.context = context;
//        this.t_id = t_id;
//        this.t_name = t_name;
//        this.t_date = t_date;
//        this.t_time = t_time;
//        this.t_detail = t_detail;
//    }


    public CustomAdapter(Context context, List<TaskModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(String.valueOf(model.get(position).getName()));
        holder.date.setText(String.valueOf(model.get(position).getDate()));
        holder.time.setText(String.valueOf(model.get(position).getTime()));
        holder.detail.setText(String.valueOf(model.get(position).getDetail()));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, time, detail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            detail = itemView.findViewById(R.id.detail);
        }
    }
}