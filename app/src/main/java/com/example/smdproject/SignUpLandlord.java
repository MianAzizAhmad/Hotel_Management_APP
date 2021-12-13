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

public class SignUpLandlord extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputHostelName;
    private TextInputLayout textInputHostelLocation;
    private TextInputLayout textInputHostelID;
    private DatabaseReference dataRef;
    private Landlord landlord;
    Boolean Checker = false;

    EBroadcastReceiver exampleBroadcastReceiver = new EBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuplandlord);

        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputHostelName = findViewById(R.id.text_input_hostelName);
        textInputHostelLocation = findViewById(R.id.text_input_hostelLocation);
        textInputHostelID = findViewById(R.id.text_input_hostelId);
        dataRef = FirebaseDatabase.getInstance().getReference().child("Landlords");

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

    private boolean validateHostelName() {
        String HostelNameInput = textInputHostelName.getEditText().getText().toString().trim();

        if (HostelNameInput.isEmpty()) {
            textInputHostelName.setError("Field can't be empty");
            return false;
        } else {
            textInputHostelName.setError(null);
            return true;
        }
    }

    private boolean ValidateHostelLocation() {
        String HostelLocationInput = textInputHostelLocation.getEditText().getText().toString().trim();

        if (HostelLocationInput.isEmpty()) {
            textInputHostelLocation.setError("Field can't be empty");
            return false;
        } else {
            textInputHostelLocation.setError(null);
            return true;
        }
    }

    private boolean validateID() {
        String Hostel_ID = textInputHostelID.getEditText().getText().toString().trim();

        if (Hostel_ID.isEmpty()) {
            textInputHostelID.setError("Field can't be empty");
            return false;
        } else if (Hostel_ID.length() > 6) {
            textInputHostelID.setError("ID Cannot be greater than 6 digits");
            return false;
        } else {
            textInputHostelID.setError(null);
            return true;
        }
    }




    public void confirmInput(View v) {
        if (!validateUsername() | !validatePassword() | !validateHostelName() |  !ValidateHostelLocation() | !validateID()) {
            return;
        }

        landlord = new Landlord();
        landlord.Name = textInputUsername.getEditText().getText().toString().trim();
        landlord.Password = textInputPassword.getEditText().getText().toString().trim();
        landlord.Hostel_ID = textInputHostelID.getEditText().getText().toString().trim();
        landlord.Hostel_Name = textInputHostelName.getEditText().getText().toString().trim();
        landlord.Location = textInputHostelLocation.getEditText().getText().toString().trim();

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(SignUpLandlord.this, "Username Match Started", Toast.LENGTH_SHORT).show();
                if(snapshot.hasChild(landlord.Name)) {
                    textInputUsername.setError("User Already Exists");
                }else{
                    if(HostelIDChecker(landlord.Hostel_ID) == true){
                        textInputHostelID.setError("HostelID Already Exists");
                    }else {
                        dataRef.child(landlord.Name).setValue(landlord);
                        Intent a = new Intent(SignUpLandlord.this, SignInActivity.class);
                        startActivity(a);
                    }
                }
                Toast.makeText(SignUpLandlord.this, "UserName Match Ended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpLandlord.this, "User Matched Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setButtons() {
        Button loginbutton = (Button) findViewById(R.id.loginButton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpLandlord.this, SignInActivity.class);
                startActivity(i);
            }
        });

        Button GotoStudentSignupButton = (Button) findViewById(R.id.GotoStudentSignupButton);
        GotoStudentSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpLandlord.this, SignUpStudent.class);
                startActivity(i);
            }
        });
    }

    public Boolean HostelIDChecker(String Hostel_Id){
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    if(postSnapShot.child("Hostel_ID").getValue(String.class).equals(Hostel_Id)) {
                        Checker = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Checker;
    }

    public class Landlord{
        public String Name;
        public String Hostel_Name;
        public String Password;
        public String Hostel_ID;
        public String Location;

        Landlord(){ }
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