package com.sharmaumang.hospital_bed_trackker.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.sharmaumang.hospital_bed_trackker.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalRegistration extends AppCompatActivity {

    CircleImageView mCircleImageView;
    TextInputEditText mHospitalName,mHospitalId,mPassword;
    Button mSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registration);

        mCircleImageView = findViewById(R.id.ivUploadedImageHos);
        mHospitalName = findViewById(R.id.et_f_name_emplee);
        mHospitalId = findViewById(R.id.et_id_emplee);
        mPassword = findViewById(R.id.et_pass_emplee);
        mSubmit = findViewById(R.id.hos_submit);

        String txt_hospital_id_fromLogin = getIntent().getExtras().getString("Email","defaultKey");
        mHospitalId.setText(txt_hospital_id_fromLogin);


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
                    Send(txt_hospital_name, txt_hospital_id, txt_password);
                }
            }
        });

    }

    private void Send (final String hospitalName, String email, String password){

        Intent mIntent = new Intent(HospitalRegistration.this, HospitalRegistration2.class);
        mIntent.putExtra("Hospital Name",hospitalName);
        mIntent.putExtra("Email",email);
        mIntent.putExtra("Password",password);
        startActivity(mIntent);

    }


}