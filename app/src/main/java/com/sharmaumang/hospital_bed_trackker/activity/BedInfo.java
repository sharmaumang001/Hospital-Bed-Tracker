package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sharmaumang.hospital_bed_trackker.R;

public class BedInfo extends AppCompatActivity {

    TextInputEditText mTotalBeds, mAvailableBeds;
    Button mUpdate;
    String txtTotalBeds,txtAvailableBeds;

    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_info);

        mTotalBeds = findViewById(R.id.et_number);
        mAvailableBeds = findViewById(R.id.et_model);
        mUpdate = findViewById(R.id.btn_details_submit);

        txtTotalBeds = mTotalBeds.getText().toString();
        txtAvailableBeds = mAvailableBeds.getText().toString();

        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData(txtTotalBeds,txtAvailableBeds,mDatabaseRef);
            }
        });

    }

    public void UpdateData(String totalBeds,String availableBeds,DatabaseReference databaseRef){

        databaseRef.child("Users").child(mFirebaseUser.getUid()).child("Total Number of beds").setValue(totalBeds)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BedInfo.this,"Total Beds updated",Toast.LENGTH_SHORT);

                    }
                });


        databaseRef.child("Users").child(mFirebaseUser.getUid()).child("Number of beds available").setValue(availableBeds)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BedInfo.this,"Available Beds updated",Toast.LENGTH_SHORT);
                    }
                });



    }

}