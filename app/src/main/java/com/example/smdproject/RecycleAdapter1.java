package com.example.smdproject;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter1 extends RecyclerView.Adapter<RecycleAdapter1.MyViewHolder>{
    private ArrayList<Room> rooms;
    final String TAG = RecycleAdapter1.class.getSimpleName();

    public RecycleAdapter1(ArrayList<Room> list2) {
        this.rooms = list2;
    }

    public Room getItem(int position){
        return rooms.get(position);
    }
    public ArrayList<Room> getList() {
        return rooms;
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    @Override
    public RecycleAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listRoom = layoutInflater.inflate(R.layout.owner_rooms, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listRoom);
        return viewHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Room room = getItem(position);
        holder.textView1.setText(room.getID());
        if(room == null) {
            Log.e(TAG, "Error at position " + position);
        }
        if (room.getStatus() == true) {
            holder.textView2.setText("Rented");
        }else{
            holder.textView2.setText("Empty");
        }
        holder.textView3.setText(room.getPrice());

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView1 = (TextView) itemView.findViewById(R.id.roomNo);
            this.textView2 = (TextView) itemView.findViewById(R.id.availabilty);
            this.textView3 = (TextView) itemView.findViewById(R.id.rent);
        }
    }
}
