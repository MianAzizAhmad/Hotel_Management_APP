package com.example.smdproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpStudent extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private DatabaseReference dataRef2;

    EBroadcastReceiver exampleBroadcastReceiver = new EBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupstudent);

        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        dataRef2 = FirebaseDatabase.getInstance().getReference().child("Students");

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
        } else {
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






    public void confirmInputStd(View v) {
        if (!validateUsername() | !validatePassword() ) {
            return;
        }

        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        dataRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(SignUpStudent.this, "Username Match Started", Toast.LENGTH_SHORT).show();
                if(snapshot.hasChild(usernameInput)) {
                    textInputUsername.setError("User Already Exists");
                }else{
                    Student student = new Student();
                    student.Name = usernameInput;
                    student.Password = passwordInput;
                    student.Hostel_ID = "-1";
                    student.Room_ID = "-1";
                    dataRef2.child(usernameInput).setValue(student);
                    Intent i = new Intent(SignUpStudent.this, SignInActivity.class);
                    startActivity(i);
                }
                Toast.makeText(SignUpStudent.this, "UserName Match Ended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpStudent.this, "User Matched Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void setButtons() {
        Button loginbutton = (Button) findViewById(R.id.loginButton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpStudent.this, SignInActivity.class);
                startActivity(i);
            }
        });



        Button GotolandlordSignupButton = (Button) findViewById(R.id.GotolandlordSignupButton);
        GotolandlordSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpStudent.this, SignUpLandlord.class);
                startActivity(i);
            }
        });
    }

    public class Student {
        public String Name;
        public String Password;
        public String Room_ID;
        public String Hostel_ID;
        public String RatingSet;
        public String Due_Rent;

        Student(){
            this. RatingSet = "0";
            this.Due_Rent = "-1";
        }
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

}