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

public class HospitalRegistration2 extends AppCompatActivity {

    CircleImageView mCircleImageView;
    TextInputEditText mEmplName,mEmplId,mEmplPhone;
    Button mSubmit;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registration2);


        mCircleImageView = findViewById(R.id.ivUploadedImageemp);
        mEmplName = findViewById(R.id.et_f_name_emplee2);
        mEmplId = findViewById(R.id.et_id_emplee2);
        mEmplPhone = findViewById(R.id.et_pass_emplee2);
        mSubmit = findViewById(R.id.empl_submit);
        auth = FirebaseAuth.getInstance();



        //Data from Intent

        String txt_hospital_name = getIntent().getExtras().getString("Hospital Name","defaultKey");
        String txt_hospital_id = getIntent().getExtras().getString("Email","defaultKey");
        String txt_password = getIntent().getExtras().getString("Password","defaultKey");


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_employee_name = mEmplName.getText().toString();
                String txt_employee_id = mEmplId.getText().toString();
                String txt_Phone = mEmplPhone.getText().toString();

                if (TextUtils.isEmpty(txt_employee_name) || TextUtils.isEmpty(txt_employee_id) || TextUtils.isEmpty(txt_Phone)){
                    Toast.makeText(HospitalRegistration2.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_Phone.length() < 6 ){
                    Toast.makeText(HospitalRegistration2.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    Register(txt_hospital_name,txt_hospital_id,txt_password,txt_employee_name, txt_employee_id, txt_Phone);
                }
            }
        });

    }



    private void Register(final String hospitalName, String email, String password,String employeeName,String employeeId ,String employeePhone){

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
                            hashMap.put("Employee Name", employeeName);
                            hashMap.put("Employee ID",employeeId);
                            hashMap.put("Empliyee Phone Number", employeePhone);


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(HospitalRegistration2.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(HospitalRegistration2.this, HospitalBedUpdate.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(HospitalRegistration2.this, "You can't register with this Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}