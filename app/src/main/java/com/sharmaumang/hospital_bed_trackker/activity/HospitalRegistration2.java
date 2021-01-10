package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sharmaumang.hospital_bed_trackker.R;
import com.sharmaumang.hospital_bed_trackker.activity.HospitalBedUpdate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalRegistration2 extends AppCompatActivity {

    CircleImageView mCircleImageView;
    TextInputEditText mEmplName,mEmplId,mEmplPhone;
    Button mSubmit;

    Uri FilePathUri;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;

    StorageTask<UploadTask.TaskSnapshot> uploadTask;
    StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    String pin="not provided";
    String district="not provided";
    final int REQUEST_CODE_FINE_LOCATION = 12;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int UNIQUE_REQUEST_CODE = 1;

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


        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_employee_name = mEmplName.getText().toString();
                String txt_employee_id = mEmplId.getText().toString();
                String txt_Phone = mEmplPhone.getText().toString();

                if (TextUtils.isEmpty(txt_employee_name) || TextUtils.isEmpty(txt_employee_id) || TextUtils.isEmpty(txt_Phone)){
                    Toast.makeText(HospitalRegistration2.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_Phone.length() != 10 ){

                    Toast.makeText(HospitalRegistration2.this, "Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
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
                            firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("Hospital UID", userId);
                            hashMap.put("Hospital ID", email);
                            hashMap.put("Hospital Name", hospitalName);
                            hashMap.put("Total Number of beds", "0");
                            hashMap.put("Number of beds available", "0");
                            hashMap.put("ID CARD URL", "0");
                            hashMap.put("Data-Time", "0");
                            hashMap.put("Employee Name", employeeName);
                            hashMap.put("Employee ID",employeeId);
                            hashMap.put("Employee Phone Number", employeePhone);


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){


                                        uploadImage();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(HospitalRegistration2.this, "You can't register with this Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    //to fetch location of user

    @SuppressLint("MissingPermission")
    private void getLocation() {

        if (ContextCompat.checkSelfPermission(HospitalRegistration2.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(HospitalRegistration2.this
                                    ,Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            pin = addresses.get(0).getPostalCode();
                            district = addresses.get(0).getLocality();


                            Toast.makeText(HospitalRegistration2.this, "PIN : "+pin+"DISTRICT : "+district, Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        else {
            ActivityCompat.requestPermissions(HospitalRegistration2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, UNIQUE_REQUEST_CODE);
        }
    }


    //check for permission available or not

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==UNIQUE_REQUEST_CODE)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getLocation();
                Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
            }
            else if(grantResults[0]==PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HospitalRegistration2.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This permission is important.");

                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(HospitalRegistration2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, UNIQUE_REQUEST_CODE);
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }



        }
    }


    //Gallery Intent Reply

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                mCircleImageView.setImageBitmap(bitmap);
                Glide.with(HospitalRegistration2.this).load(FilePathUri).into(mCircleImageView);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(HospitalRegistration2.this);
        pd.setMessage("Uploading");
        pd.show();
        pd.setCancelable(false);

        if (FilePathUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+GetFileExtension(FilePathUri));

            uploadTask = fileReference.putFile(FilePathUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){

                        throw  task.getException();

                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("ID CARD URL", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();

                        Toast.makeText(HospitalRegistration2.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(HospitalRegistration2.this, HospitalBedUpdate.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(HospitalRegistration2.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HospitalRegistration2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(HospitalRegistration2.this, "Please add your ID card Image by Adding", Toast.LENGTH_SHORT).show();
        }
    }


}