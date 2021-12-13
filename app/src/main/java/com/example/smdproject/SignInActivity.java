package com.example.smdproject;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private Boolean firstTime = null;
    private TextInputLayout textInputPassword;
    private DatabaseReference dataRef;
    private DatabaseReference dataRef2;
    private String Hostel_ID;
    private String Room_ID;
    private String Name;
    private String Location;
    private String HostelName;
    private static final String TAG = "SignInActivity";

    EBroadcastReceiver exampleBroadcastReceiver = new EBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        setTitle("                     Students_Hostel");
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        dataRef = FirebaseDatabase.getInstance().getReference().child("Landlords");
        dataRef2 = FirebaseDatabase.getInstance().getReference().child("Students");
        if (isFirstTime()) {

            scheduleDayChanger();
            //Toast.makeText(SignInActivity.this, "Running First Time", Toast.LENGTH_SHORT).show();
        }
        setButtons();
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username too long");
            return false;
        }
        else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }else{
                firstTime = false;
            }
        }
        return firstTime;
    }

    public void confirmInput(View v) {
        if (!validateUsername() | !validatePassword()) {
            return;
        }
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();


        RadioButton studentbutton = (RadioButton) findViewById(R.id.radioButtonStudent);
        RadioButton landlordbutton = (RadioButton) findViewById(R.id.radioButtonStudent);
        if(studentbutton.isChecked())       //Student
        {
            //code to check whether username and password match in firebase
            //if already present return false
            dataRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(SignInActivity.this, "Username Match Started", Toast.LENGTH_SHORT).show();
                        if(snapshot.hasChild(usernameInput)) {
                            if(snapshot.child(usernameInput).child("Password").getValue(String.class).equals(passwordInput)){
                                Name = snapshot.child(usernameInput).child("Name").getValue(String.class);
                                Hostel_ID = snapshot.child(usernameInput).child("Hostel_ID").getValue(String.class);
                                Room_ID = snapshot.child(usernameInput).child("Room_ID").getValue(String.class);
                                String setrating = snapshot.child(usernameInput).child("RatingSet").getValue(String.class);
                                String Due_Rent = snapshot.child(usernameInput).child("Due_Rent").getValue(String.class);
                                Intent a = new Intent(SignInActivity.this, StudentActivity.class);
                                a.putExtra("Name",Name);
                                a.putExtra("Room_ID",Room_ID);
                                a.putExtra("Hostel_ID",Hostel_ID);
                                a.putExtra("RatingSet",setrating);
                                a.putExtra("Due_Rent",Due_Rent);
                                startActivity(a);
                            }else{
                                textInputPassword.setError("Given Password Wrong!");
                            }
                        }else{
                            textInputUsername.setError("Given Username Wrong!");
                        }
                    Toast.makeText(SignInActivity.this, "UserName Match Ended", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignInActivity.this, "User Matched Failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else       //landlord
        {
            //code to check whether username and password match in firebase
            //if already present return false
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(SignInActivity.this, "Username Match Started", Toast.LENGTH_SHORT).show();
                        if(snapshot.hasChild(usernameInput)) {
                            if(snapshot.child(usernameInput).child("Password").getValue(String.class).equals(passwordInput)){
                                Name = snapshot.child(usernameInput).child("Name").getValue(String.class);
                                Hostel_ID = snapshot.child(usernameInput).child("Hostel_ID").getValue(String.class);
                                Location = snapshot.child(usernameInput).child("Location").getValue(String.class);
                                HostelName = snapshot.child(usernameInput).child("Hostel_Name").getValue(String.class);
                                Intent a = new Intent(SignInActivity.this, LandlordActivity.class);
                                a.putExtra("Name",Name);
                                a.putExtra("Location",Location);
                                a.putExtra("Hostel_ID",Hostel_ID);
                                a.putExtra("Hostel_Name",HostelName);
                                startActivity(a);
                            }else{
                                textInputPassword.setError("Given Password Wrong!");
                            }
                        }else{
                            textInputUsername.setError("Given Username Wrong!");
                        }
                    Toast.makeText(SignInActivity.this, "UserName Match Ended", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignInActivity.this, "User Matched Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    public void setButtons() {
        Button signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton landButton = (RadioButton) findViewById(R.id.radioButtonLandLord);
                RadioButton studButton = (RadioButton) findViewById(R.id.radioButtonStudent);

                if(studButton.isChecked())       //Student
                {
                    Intent i = new Intent(SignInActivity.this, SignUpStudent.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(SignInActivity.this, SignUpLandlord.class);
                    startActivity(i);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(exampleBroadcastReceiver, filter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(exampleBroadcastReceiver);
    }

    public void scheduleDayChanger(){
        ComponentName compName = new ComponentName(this, RoomJobService.class);
        JobInfo job = new JobInfo.Builder(6599, compName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();
        JobScheduler jobschedule = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int result = jobschedule.schedule(job);
        if(result == jobschedule.RESULT_SUCCESS){
            Log.d(TAG, "Job successful");
        }else{
            Log.d(TAG, "Job unsuccessful");
        }

    }

    public void cancelDayChanger(){
        JobScheduler jobschedule = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobschedule.cancel(6599);
        Log.d(TAG, "Job cancelled");
    }

}