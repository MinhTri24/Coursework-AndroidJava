package com.example.coursework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList id;
    private final ArrayList name;
    private final ArrayList location;
    private final ArrayList doh;
    private final ArrayList parking;
    private final ArrayList length;
    private final ArrayList toh;
    private final ArrayList participants;
    private final ArrayList difficulty;
    private final ArrayList description;

    CustomAdapter(Context context, ArrayList id, ArrayList name, ArrayList location, ArrayList doh, ArrayList demoParking, ArrayList length, ArrayList toh, ArrayList participants, ArrayList demoDifficulty, ArrayList description){
        this.context = context;
        this.id = id;
        this.name = name;
        this.location = location;
        this.doh = doh;
        this.parking = demoParking;
        this.length = length;
        this.toh = toh;
        this.participants = participants;
        this.difficulty = demoDifficulty;
        this.description = description;
    }


    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.name_txt.setText(String.valueOf(name.get(position)));
        holder.location_txt.setText(String.valueOf(location.get(position)));
        holder.doh_txt.setText(String.valueOf(doh.get(position)));
        holder.parking_txt.setText(String.valueOf(parking.get(position)));
        holder.length_txt.setText(String.valueOf(length.get(position)));
        holder.difficulty_txt.setText(String.valueOf(difficulty.get(position)));
        holder.toh_txt.setText(String.valueOf(toh.get(position)));
        holder.participants_txt.setText(String.valueOf(participants.get(position)));
        holder.description_txt.setText(String.valueOf(description.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("name", String.valueOf(name.get(position)));
                intent.putExtra("location", String.valueOf(location.get(position)));
                intent.putExtra("doh", String.valueOf(doh.get(position)));
                intent.putExtra("length", String.valueOf(length.get(position)));
                intent.putExtra("toh", String.valueOf(toh.get(position)));
                intent.putExtra("participants", String.valueOf(participants.get(position)));
                intent.putExtra("description", String.valueOf(description.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_txt, doh_txt, location_txt, length_txt, description_txt, difficulty_txt, parking_txt, toh_txt, participants_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.hike_name_txt);
            location_txt = itemView.findViewById(R.id.hike_location_txt);
            doh_txt = itemView.findViewById(R.id.doh_txt);
            parking_txt = itemView.findViewById(R.id.parking_txt);
            length_txt = itemView.findViewById(R.id.length_txt);
            difficulty_txt = itemView.findViewById(R.id.difficult_txt);
            toh_txt = itemView.findViewById(R.id.time_txt);
            participants_txt = itemView.findViewById(R.id.participants_txt);
            description_txt = itemView.findViewById(R.id.description_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
