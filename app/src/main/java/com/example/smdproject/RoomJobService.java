package com.example.smdproject;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RoomJobService extends JobService {
    private static final String TAG = "RoomJobService";
    private Boolean jobCancelled = false;
    private DatabaseReference dataRef2;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Rent Due Changer started");
        dataRef2 = FirebaseDatabase.getInstance().getReference().child("Students");
        if(!jobCancelled) {
            changedDeDates();
            jobFinished(params,false);
        }
        return true;
    }

    public void changedDeDates(){
        dataRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    if(!postSnapShot.child("Room_ID").getValue(String.class).equals("-1")){
                        String Days = postSnapShot.child("Due_Rent").getValue(String.class);
                        int day = Integer.parseInt(Days);
                        if(day == 0){
                            dataRef2.child(postSnapShot.getKey()).child("Due_Rent").setValue("30");
                        }else{
                            day = day - 1 ;
                            dataRef2.child(postSnapShot.getKey()).child("Due_Rent").setValue(String.valueOf(day));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG,"Rent Due Changer cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
