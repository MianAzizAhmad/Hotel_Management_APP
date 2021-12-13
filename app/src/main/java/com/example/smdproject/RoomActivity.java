package com.example.smdproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;

public class RoomActivity extends AppCompatActivity {
    private Room room;
    private TextInputLayout id;
    private TextInputLayout dimensions;
    private TextInputLayout price;
    private CheckBox AC;
    private ImageView image;
    private ImageButton imageButton;
    private CheckBox WiFi;
    private Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storeRef;
    private DatabaseReference dataRef;
    private String url;
    public String Hostelid;
    public String loc;
    public String ratings;

    private ProgressBar uploading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("                      Room Details");
        setContentView(R.layout.room_add);

        dataRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        storeRef = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        Hostelid = intent.getStringExtra("Hostel_ID");
        ratings = intent.getStringExtra("ratings");
        loc = intent.getStringExtra("location");

        this.id = findViewById(R.id.roomId);
        this.dimensions = findViewById(R.id.RoomDim);
        this.price = findViewById(R.id.Rate);
        this.AC = findViewById(R.id.AC);
        this.WiFi = findViewById(R.id.WiFi);
        this.image = findViewById(R.id.roomImage);
        this.uploading = findViewById(R.id.progressBar);
        this.imageButton = findViewById(R.id.addImage);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

    }

    private void openImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() != null){
            imageUri = data.getData();
            this.image.setImageURI(imageUri);
        }
    }

    private Boolean idChecker(){
        String ID = this.id.getEditText().getText().toString().trim();
        if(ID.isEmpty()){
            this.id.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

    private Boolean priceChecker(){
        String price = this.price.getEditText().getText().toString().trim();
        if(price.isEmpty()){
            this.price.setError("Field cannot be empty");
            return false;
        }
        return true;
    }

    private Boolean dimsChecker(){
        String dims = this.dimensions.getEditText().getText().toString().trim();
        if(dims.isEmpty()){
            this.dimensions.setError("Field cannot be empty");
            return false;
        }else{
            int slash = dims.indexOf('x');
            String stripped = dims.substring(0,slash);
            String stripped2 = dims.substring(slash + 1,dims.length());
            if(TextUtils.isDigitsOnly(stripped)==true && TextUtils.isDigitsOnly(stripped2)==true){
                return true;
            }else{
                this.dimensions.setError("Dimensions in not correct Format");
                return false;
            }
        }
    }

    public void confirmInput(View v){
        if(!dimsChecker() | !priceChecker() | !idChecker()){
            return;
        }

        room = new Room(AC.isChecked(),WiFi.isChecked(),id.getEditText().getText().toString().trim(),false,dimensions.getEditText().getText().toString().trim(), price.getEditText().getText().toString().trim(),"",ratings,loc,Hostelid);
        uploadFile(room);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },5000);
    }


    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void uploadFile(Room room){
        if(imageUri !=null){
            final String TAG = RoomActivity.class.getSimpleName();
            String time = String.valueOf(System.currentTimeMillis());
            StorageReference imageRef = storeRef.child( time+ "." + getFileExtension(imageUri));
            imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference ref = storeRef.child( time+ "." + getFileExtension(imageUri));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploading.setProgress(0);
                        }
                    },3000);
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downUrl = uri;
                            url = downUrl.toString();
                            //room.setImage(downUrl.toString());
                            String roomID = Hostelid + "(" + id.getEditText().getText().toString().trim() +")";
                            room.imageUrl = url;
                            //dataRef.child(roomID).setValue(url);
                            dataRef.child(roomID).setValue(room);
                            //Toast.makeText(RoomActivity.this,url, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Exception : "+ e.getMessage());
                            Toast.makeText(RoomActivity.this,"Room Addition Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Toast.makeText(RoomActivity.this,"Room Addition Completed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RoomActivity.this,"File Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress  = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    uploading.setProgress((int)progress);
                }
            });
        }else{
            Toast.makeText(RoomActivity.this,"No file Selected", Toast.LENGTH_SHORT).show();
        }
    }

}
