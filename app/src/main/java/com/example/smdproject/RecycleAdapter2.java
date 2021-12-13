package com.example.smdproject;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter2 extends RecyclerView.Adapter<RecycleAdapter2.MyViewHolder> implements Filterable {
    private ArrayList<Room> rooms;
    private ArrayList<Room> copiedRooms;
    final String TAG = RecycleAdapter1.class.getSimpleName();
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener){
        listener = mListener;
    }

    public RecycleAdapter2(ArrayList<Room> list2) {
        this.rooms = list2;
        copiedRooms = new ArrayList<>(list2);
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
    public RecycleAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listRoom = layoutInflater.inflate(R.layout.listview2, parent, false);
        RecycleAdapter2.MyViewHolder viewHolder = new RecycleAdapter2.MyViewHolder(listRoom);
        return viewHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecycleAdapter2.MyViewHolder holder, int position) {
        Room room = getItem(position);
        holder.textView1.setText(room.Location);
        holder.textView2.setText(room.ID);
        holder.textView3.setText(room.Ratings);
        holder.textView4.setText(room.getPrice());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textView1 = (TextView) itemView.findViewById(R.id.location);
            this.textView2 = (TextView) itemView.findViewById(R.id.RoomID);
            this.textView3 = (TextView) itemView.findViewById(R.id.ratings);
            this.textView4 = (TextView) itemView.findViewById(R.id.rentFee);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return roomFilter;
    }

    private Filter roomFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Room> filteredRooms = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredRooms.addAll(copiedRooms);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Room room : copiedRooms){
                    if(room.Location.toLowerCase().contains(filterPattern)){
                        filteredRooms.add(room);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredRooms;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rooms.clear();
            rooms.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
