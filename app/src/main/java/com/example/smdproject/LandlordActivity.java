package com.example.smdproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class LandlordActivity extends AppCompatActivity {
    public ArrayList<Room> rooms;
    RecycleAdapter1 adapter;
    public TextView textView1;
    public TextView textView2;
    private TextView textView3;
    final int REQUEST_CODE = 1;
    private DatabaseReference dataRef;
    public String Hostel_Id;
    public String loc;
    public String Name;
    public String HostelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hostel Details");
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Hostel_Id = intent.getStringExtra("Hostel_ID");
        Name = intent.getStringExtra("Name");
        loc = intent.getStringExtra("Location");
        HostelName = intent.getStringExtra("Hostel_Name");

        this.textView1 = (TextView) findViewById(R.id.hotelname);
        this.textView1.setText(HostelName);
        this.textView3 = (TextView) findViewById(R.id.Name);
        this.textView3.setText(Name);
        this.textView2 = (TextView) findViewById(R.id.address);
        this.textView2.setText(loc);
        dataRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        dataFill();

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LandlordActivity.this, RoomActivity.class);
                intent.putExtra("location",loc);
                intent.putExtra("ratings","0");
                intent.putExtra("Hostel_ID",Hostel_Id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_out, menu);
        return true;
    }
    //Code for logging out
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.log_out){
            Intent intent = new Intent( LandlordActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void BuildRecycleView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView1);
        adapter = new RecycleAdapter1(rooms);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void dataRetrieve(){
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(LandlordActivity.this, "Data Retrievel Started", Toast.LENGTH_SHORT).show();
                if(rooms!=null){
                    rooms.clear();
                }
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    Room room = new Room();
                    if(postSnapShot.child("Hostel_ID").getValue(String.class).equals(Hostel_Id)) {
                        room.AC = postSnapShot.child("AC").getValue(Boolean.class);
                        room.Status = postSnapShot.child("Status").getValue(Boolean.class);
                        room.WiFi = postSnapShot.child("WiFi").getValue(Boolean.class);
                        room.Price = postSnapShot.child("Price").getValue(String.class);
                        room.ID = postSnapShot.child("ID").getValue(String.class);
                        room.Location = postSnapShot.child("Location").getValue(String.class);
                        room.Hostel_ID = postSnapShot.child("Hostel_ID").getValue(String.class);
                        room.Dimensions = postSnapShot.child("Dimensions").getValue(String.class);
                        rooms.add(room);
                    }

                }
                Toast.makeText(LandlordActivity.this, "Data Retrievel Ended", Toast.LENGTH_SHORT).show();
                BuildRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LandlordActivity.this, "Data Retrievel Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void dataFill(){
        if(rooms==null) {
            rooms = new ArrayList<Room>();
            dataRetrieve();
        }else {
            rooms.clear();
            dataRetrieve();
        }
    }

}