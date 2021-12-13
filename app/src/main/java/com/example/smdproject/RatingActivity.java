package com.example.smdproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {

    RatingBar rat;
    Button btn;

    private String Hostel_ID;
    private String Room_ID;
    private String Name;
    private String strRatings;
    private String strTotal;
    private DatabaseReference dataRef;
    private DatabaseReference dataRef3;
    private float newRatings;
    private String newTotal;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingactivity);
        setTitle("                         RATING");
        dataRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        dataRef3 = FirebaseDatabase.getInstance().getReference().child("Students");
        Intent intent = getIntent();
        Hostel_ID = intent.getStringExtra("Hostel_ID");
        Room_ID = intent.getStringExtra("Room_ID");
        Name = intent.getStringExtra("Name");
        rat=findViewById(R.id.ratingBar);
        btn=findViewById(R.id.rtsubmit);

//Performing action on Button Click
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Getting the rating and displaying it on the toast
                float rating = rat.getRating();
                getoldratings(rating);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String s = String.format("%.2f", newRatings);
                        dataRef.child(key).child("Ratings").setValue(s);
                        dataRef.child(key).child("totalresidents").setValue(newTotal);
                        dataRef3.child(Name).child("RatingSet").setValue("1");
                        finish();
                    }
                },2000);
                Toast.makeText(RatingActivity.this, "button Pressed", Toast.LENGTH_SHORT).show();

            }


        });
    }


    public void getoldratings(float stars){
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                key = Hostel_ID+"("+Room_ID+")";
                if(snapshot.hasChild(key)) {
                    strRatings = snapshot.child(key).child("Ratings").getValue(String.class);
                    strTotal = snapshot.child(key).child("totalresidents").getValue(String.class);
                    float ratings = Float.parseFloat(strRatings);
                    int total = Integer.parseInt(strTotal);
                    newRatings = ((ratings*total) + stars)/(total + 1);
                    newTotal = String.valueOf(total +1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RatingActivity.this, "Old Rating Retrieval Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}