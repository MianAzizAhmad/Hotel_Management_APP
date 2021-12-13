package com.example.smdproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentActivity extends AppCompatActivity {

    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    private DatabaseReference dataRef;
    private DatabaseReference dataRef2;
    private DatabaseReference dataRef3;
    private String Name;
    private String Hostel_ID;
    private String Due_Rent;
    private String Room_ID;
    private String fee;
    private String Hostel_Name;
    private String RatingSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentactivity);
        setTitle("                      STUDENT INFO");
        text1=findViewById(R.id.CRuserInfo);
        text2=findViewById(R.id.CrHostelInfo);
        text3=findViewById(R.id.CRRommInfo);
        text4=findViewById(R.id.RentFeeInfo);
        text5=findViewById(R.id.Days);
        dataRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        dataRef2 = FirebaseDatabase.getInstance().getReference().child("Landlords");
        dataRef3 = FirebaseDatabase.getInstance().getReference().child("Students");
        Intent a = getIntent();
        Name = a.getStringExtra("Name");
        Hostel_ID = a.getStringExtra("Hostel_ID");
        Room_ID = a.getStringExtra("Room_ID");
        RatingSet = a.getStringExtra("RatingSet");
        Due_Rent = a.getStringExtra("Due_Rent");
        if(!Hostel_ID.equals("-1") && !Room_ID.equals("-1")) {
            getfee();
            gethostelname();
            text1.setText(Name);
            text3.setText(Room_ID);
            text5.setText(Due_Rent);
        }else{
            text1.setText(Name);
            text2.setText("None");
            text3.setText("None");
            text4.setText("None");
            text5.setText("None");
        }
        Button load = findViewById(R.id.Rater);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Hostel_ID.equals("-1") && !Room_ID.equals("-1")) {
                    if(RatingSet.equals("0")) {
                        Intent loading = new Intent(StudentActivity.this, RatingActivity.class);
                        loading.putExtra("Room_ID", Room_ID);
                        loading.putExtra("Name", Name);
                        loading.putExtra("Hostel_ID", Hostel_ID);
                        startActivity(loading);
                    }else{
                        Toast.makeText(StudentActivity.this, "Already Rated", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(StudentActivity.this, "No Room to Rate", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button more = findViewById(R.id.More);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Hostel_ID.equals("-1") && Room_ID.equals("-1")) {
                    Intent loading = new Intent(StudentActivity.this, ListActivity.class);
                    loading.putExtra("Name", Name);
                    startActivity(loading);
                }else{
                    Toast.makeText(StudentActivity.this, "Please Leave Room First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button logout = findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loading = new Intent(StudentActivity.this, SignInActivity.class);
                loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loading);
            }
        });

        Button leave = findViewById(R.id.Leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Hostel_ID.equals("-1") && !Room_ID.equals("-1")) {
                    String key = Hostel_ID+"("+Room_ID+")";
                    dataRef.child(key).child("Status").setValue(false);
                    dataRef3.child(Name).child("Hostel_ID").setValue("-1");
                    dataRef3.child(Name).child("Room_ID").setValue("-1");
                    dataRef3.child(Name).child("RatingSet").setValue("0");
                    leaveactivity();
                }else{
                    Toast.makeText(StudentActivity.this, "No Room to Leave", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void leaveactivity(){
        Intent loading = new Intent(StudentActivity.this, SignInActivity.class);
        loading.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loading);
    }

    public void getfee(){
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = Hostel_ID+"("+Room_ID+")";
                if(snapshot.hasChild(key)) {
                    fee = snapshot.child(key).child("Price").getValue(String.class);
                    text4.setText(fee);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentActivity.this, "Student Room Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gethostelname(){
        dataRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    if(postSnapShot.child("Hostel_ID").getValue(String.class).equals(Hostel_ID)) {
                        Hostel_Name = postSnapShot.child("Hostel_Name").getValue(String.class);
                        text2.setText(Hostel_Name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentActivity.this, "Student Hostel Name Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}