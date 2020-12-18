package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.sharmaumang.hospital_bed_trackker.R;
import com.sharmaumang.hospital_bed_trackker.activity.HospitalRegistration;

public class HospitalLogin extends AppCompatActivity {

    private TextInputLayout mail, pass;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_login);

        //changing status bar
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        mail = findViewById(R.id.mail_lay);
        pass = findViewById(R.id.pass_lay);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mail.getEditText().getText().toString();
                String password = pass.getEditText().getText().toString();

                if (TextUtils.isEmpty(mail.getEditText().getText()) || TextUtils.isEmpty(pass.getEditText().getText())) {
                    Toast.makeText(HospitalLogin.this, "Enter the auth", Toast.LENGTH_LONG).show();
                } else if (!(email.equals("abc@gmail.com")) || !(password.equals("123"))) {
                    Toast.makeText(HospitalLogin.this, "Enter the correct auth", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(HospitalLogin.this, HospitalRegistration.class));
                }

            }
        });


    }
}