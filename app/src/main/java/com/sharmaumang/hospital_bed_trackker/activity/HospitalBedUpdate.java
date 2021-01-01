package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sharmaumang.hospital_bed_trackker.R;

public class HospitalBedUpdate extends AppCompatActivity {

    TextInputEditText mTotalBeds, mAvailableBeds;
    MaterialButton mUpdate;
    String txtTotalBeds,txtAvailableBeds;

    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;
    FirebaseUser mFirebaseUser;
    FirebaseAuth auth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_bed_update);

        mTotalBeds = findViewById(R.id.et_number);
        mAvailableBeds = findViewById(R.id.et_model);
        mUpdate = findViewById(R.id.btn_details_submit);

        txtTotalBeds = mTotalBeds.getText().toString();
        txtAvailableBeds = mAvailableBeds.getText().toString();

        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();


        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtTotalBeds = mTotalBeds.getText().toString();
                txtAvailableBeds = mAvailableBeds.getText().toString();

                UpdateData(txtTotalBeds,txtAvailableBeds,mDatabaseRef);
            }
        });

    }

    public void UpdateData(String totalBeds,String availableBeds,DatabaseReference databaseRef){

        mDatabaseRef.child("Users").child(uid).child("Total Number of beds").setValue(totalBeds.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HospitalBedUpdate.this,"Total Beds updated",Toast.LENGTH_SHORT);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HospitalBedUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        mDatabaseRef.child("Users").child(uid).child("Number of beds available").setValue(availableBeds)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HospitalBedUpdate.this,"Available Beds updated",Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HospitalBedUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }



}