package com.example.bedtracker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bedtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HospitalLogin extends AppCompatActivity {

    private TextInputLayout mail,pass;
    private MaterialButton btnLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_login);


        //Non-changing status bar

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        mail = findViewById(R.id.mail_lay);
        pass = findViewById(R.id.pass_lay);
        btnLogin=findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mail.getEditText().getText().toString();
                String password = pass.getEditText().getText().toString();

                if(TextUtils.isEmpty(mail.getEditText().getText()) || TextUtils.isEmpty(pass.getEditText().getText())){
                    Toast.makeText(HospitalLogin.this,"Enter the auth",Toast.LENGTH_LONG).show();
                }

                else if((email.equals("abc@gmail.com")) && (password.equals("123"))){

                    Intent mIntent = new Intent(HospitalLogin.this,HospitalBedUpdate.class);
                    finish();
                    startActivity(mIntent);

                }else{
                    userRegistrationCheck(email,password);

                }

            }
        });

    }

    private void SignIn(String Email,String Password){


        mAuth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(HospitalLogin.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(HospitalLogin.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void userRegistrationCheck(String Email,String Password){


        DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference("Users");
        UserReference.orderByChild("Hospital ID").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){

                    SignIn(Email,Password);

                }else{

                    Intent mIntent = new Intent(HospitalLogin.this,HospitalRegistration.class);
                    mIntent.putExtra("Email",Email);
                    finish();
                    startActivity(mIntent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}