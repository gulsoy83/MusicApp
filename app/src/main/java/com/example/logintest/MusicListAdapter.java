package com.example.logintest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    ArrayList<AudioModel> musList;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> musList, Context context) {
        this.musList = musList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MusicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        AudioModel musData = musList.get(position);
        holder.titleTextView.setText(musData.getTitle());

        if(MyMediaPlayer.currentIndex == position){
            //holder.titleTextView.setTextColor(Color.parseColor("#FF6200EE"));
            holder.iconImageView.setImageResource(R.drawable.ic_baseline_audiotrack_24);
        }else
            holder.iconImageView.setImageResource(R.drawable.ic_baseline_person_24);
            //holder.titleTextView.setTextColor(Color.parseColor("#ffffff"));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.getIntstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayer.class);
                intent.putExtra("LIST",musList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return musList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        ImageView iconImageView;

        public ViewHolder( View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageView = itemView.findViewById(R.id.iconview);
        }
    }

}
