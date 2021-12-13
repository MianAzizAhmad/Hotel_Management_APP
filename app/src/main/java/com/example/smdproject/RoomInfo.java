package com.example.smdproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RoomInfo extends AppCompatActivity {

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private DatabaseReference dataRef3;
    private DatabaseReference dataRef;
    private TextView text4;
    private TextView text5;
    private Room room;
    private ImageView img;
    private String Name;
    private ProgressBar imgloading;

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roominfo);
        dataRef3 = FirebaseDatabase.getInstance().getReference().child("Students");
        dataRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        setTitle("Room Info ");
        text1 = findViewById(R.id.CrRoom);
        text2 = findViewById(R.id.Dim);
        text3 = findViewById(R.id.AC);
        text4 = findViewById(R.id.wifi);
        text5 = findViewById(R.id.RentingsInfo);
        img = findViewById(R.id.imageView);
        imgloading = findViewById(R.id.progressBar2);
        Intent a = getIntent();
        Name = a.getStringExtra("Name");
        room = (Room) a.getSerializableExtra("room");
        text1.setText(room.ID);
        text2.setText(room.Dimensions);
        if(room.AC == true) {
            text3.setText("Yes");
        }else{
            text3.setText("No");
        }
        if(room.WiFi == true) {
            text4.setText("Yes");
        }else{
            text4.setText("No");
        }
        text5.setText(room.Ratings);
        Picasso.get().load(room.imageUrl).noFade().into(img, new Callback() {
            @Override
            public void onSuccess() {
                imgloading.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(RoomInfo.this, "Image Couldn't Be Loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void bookroom(View v){
        String key = room.Hostel_ID + "(" + room.ID+")";
        dataRef.child(key).child("Status").setValue(true);
        dataRef3.child(Name).child("Hostel_ID").setValue(room.Hostel_ID);
        dataRef3.child(Name).child("Room_ID").setValue(room.ID);
        dataRef3.child(Name).child("RatingSet").setValue("0");
        dataRef3.child(Name).child("Due_Rent").setValue("30");

        Intent loading = new Intent(RoomInfo.this, StudentActivity.class);
        loading.putExtra("Room_ID", room.ID);
        loading.putExtra("Name", Name);
        loading.putExtra("Hostel_ID", room.Hostel_ID);
        loading.putExtra("RatingSet", "0");
        startActivity(loading);
    }
}