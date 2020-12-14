package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sharmaumang.hospital_bed_trackker.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalRegistration extends AppCompatActivity {

    CircleImageView mCircleImageView;
    TextInputEditText mHospitalName,mHospitalId,mPassword;
    Button mSubmit;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registration);

        mCircleImageView = findViewById(R.id.ivUploadedImageemp);
        mHospitalName = findViewById(R.id.et_f_name_emplee);
        mHospitalId = findViewById(R.id.et_id_emplee);
        mPassword = findViewById(R.id.et_pass_emplee);
        mSubmit = findViewById(R.id.empl_submit);

        auth = FirebaseAuth.getInstance();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_hospital_name = mHospitalName.getText().toString();
                String txt_hospital_id = mHospitalId.getText().toString();
                String txt_password = mPassword.getText().toString();

                if (TextUtils.isEmpty(txt_hospital_name) || TextUtils.isEmpty(txt_hospital_id) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(HospitalRegistration.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6 ){
                    Toast.makeText(HospitalRegistration.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    Register(txt_hospital_name, txt_hospital_id, txt_password);
                }
            }
        });

    }

    private void Register(final String hospitalName, String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("Hospital UID", userid);
                            hashMap.put("Hospital Name", hospitalName);
                            hashMap.put("Total Number of beds", "0");
                            hashMap.put("Number of beds available", "0");


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(HospitalRegistration.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(HospitalRegistration.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(HospitalRegistration.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}