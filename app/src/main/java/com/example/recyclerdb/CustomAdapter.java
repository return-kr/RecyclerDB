package com.example.recyclerdb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<TaskModel> model;
    Animation animation;

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.title.setText(String.valueOf(model.get(position).getName()));
        holder.date.setText(String.valueOf(model.get(position).getDate()));
        holder.time.setText(String.valueOf(model.get(position).getTime()));
        holder.detail.setText(String.valueOf(model.get(position).getDetail()));
        final byte[] img = model.get(position).getImage();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        holder.image.setImageBitmap(bitmap);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                holder.image.buildDrawingCache();
                Bitmap bt = holder.image.getDrawingCache();
                intent.putExtra("id", String.valueOf(model.get(position).getId()));
                intent.putExtra("name", String.valueOf(model.get(position).getName()));
                intent.putExtra("date", String.valueOf(model.get(position).getDate()));
                intent.putExtra("time", String.valueOf(model.get(position).getTime()));
                intent.putExtra("detail", String.valueOf(model.get(position).getDetail()));
                intent.putExtra("image", bt);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, time, detail;
        ImageView image;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            detail = itemView.findViewById(R.id.detail);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            image = itemView.findViewById(R.id.thumbnail);
            animation = AnimationUtils.loadAnimation(context, R.anim.recycler_anim);
            mainLayout.setAnimation(animation);
        }
    }
}
