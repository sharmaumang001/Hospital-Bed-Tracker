package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sharmaumang.hospital_bed_trackker.R;
import com.sharmaumang.hospital_bed_trackker.activity.Home;
import com.sharmaumang.hospital_bed_trackker.activity.HospitalBedUpdate;
import com.sharmaumang.hospital_bed_trackker.activity.HospitalLogin;

public class Welcome extends AppCompatActivity {
    private Button hospital_login,user_login;
    FirebaseUser user ;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }
        hospital_login = findViewById(R.id.hospital);
        user_login = findViewById(R.id.user);

        SharedPreferences sharedPreferences = getSharedPreferences("LASTSTATE", MODE_PRIVATE);
        String value = sharedPreferences.getString("STATE","");

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null ){
            if (value.equals("BEDUPDATE")){
            Intent intent = new Intent(Welcome.this, HospitalBedUpdate.class);
            startActivity(intent);
            finish();}

            if (value.equals("HOME")){
                Intent intent = new Intent(Welcome.this, Home.class);
                startActivity(intent);
                finish();}

        }


        hospital_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                auth = FirebaseAuth.getInstance();
//                if(auth.getCurrentUser()!=null ){
//                    Intent intent = new Intent(Welcome.this,HospitalBedUpdate.class);
//                    startActivity(intent);
//                    finish();
//                }


//                if(user!=null ){
//                    Intent intent = new Intent(Welcome.this, HospitalBedUpdate.class);
//                    startActivity(intent);
//                    finish();
//                }
                startActivity(new Intent(Welcome.this, HospitalLogin.class));
                finish();
            }
        });
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                auth = FirebaseAuth.getInstance();
//                if(auth.getCurrentUser()!=null ){
//                    Intent intent = new Intent(Welcome.this,Home.class);
//                    startActivity(intent);
//                    finish();
//                }

//                if(user!=null ){
//                    Intent intent = new Intent(Welcome.this, Home.class);
//                    startActivity(intent);
//                    finish();
//                }
                  startActivity(new Intent(Welcome.this,Home.class));
                //startActivity(new Intent(Welcome.this,MainActivity.class));
                finish();
            }
        });

    }
}