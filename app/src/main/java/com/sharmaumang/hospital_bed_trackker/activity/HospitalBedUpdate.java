package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sharmaumang.hospital_bed_trackker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HospitalBedUpdate extends AppCompatActivity {

    TextInputEditText mTotalBeds, mAvailableBeds;
    MaterialButton mUpdate;
    String txtTotalBeds,txtAvailableBeds;

    FirebaseDatabase database;
    DatabaseReference mDatabaseRef;
    FirebaseUser mFirebaseUser;
    FirebaseAuth auth;
    String uid;
    private ImageView options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_bed_update);

        mTotalBeds = findViewById(R.id.et_number);
        mAvailableBeds = findViewById(R.id.et_model);
        mUpdate = findViewById(R.id.btn_details_submit);
        options = findViewById(R.id.options);

        txtTotalBeds = mTotalBeds.getText().toString();
        txtAvailableBeds = mAvailableBeds.getText().toString();

        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();
        uid=auth.getCurrentUser().getUid();

        SharedPreferences sharedPref = getSharedPreferences("LASTSTATE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("STATE","BEDUPDATE" );
        editor.apply();







        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                txtTotalBeds = mTotalBeds.getText().toString();
                txtAvailableBeds = mAvailableBeds.getText().toString();

                UpdateData(txtTotalBeds,txtAvailableBeds,mDatabaseRef,formattedDate);



            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HospitalBedUpdate.this, OptionActivity.class));
            }
        });

    }

    public void UpdateData(String totalBeds,String availableBeds,DatabaseReference databaseRef,String DateTime){

        final ProgressDialog pd = new ProgressDialog(HospitalBedUpdate.this);
        pd.setMessage("Checking Details");
        pd.show();
        pd.setCancelable(false);

        mDatabaseRef.child("Users").child(uid).child("Total Number of beds").setValue("Total Number of Beds : "+totalBeds);

        mDatabaseRef.child("Users").child(uid).child("Number of beds available").setValue("Number of Beds Available : "+availableBeds);

        mDatabaseRef.child("Users").child(uid).child("Data-Time").setValue("Updated on : "+DateTime);


        mDatabaseRef.child("Users").child(uid).child("Data-Time").equalTo(DateTime)

                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        pd.dismiss();
                        Toast.makeText(HospitalBedUpdate.this, "Data updated successfully!", Toast.LENGTH_SHORT).show();
                        mTotalBeds.setText("");
                        mAvailableBeds.setText("");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        pd.dismiss();
                        Toast.makeText(HospitalBedUpdate.this, "Error Updating the data, please try again!", Toast.LENGTH_SHORT).show();

                    }
                });


    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//        moveTaskToBack(true);
//    }



}