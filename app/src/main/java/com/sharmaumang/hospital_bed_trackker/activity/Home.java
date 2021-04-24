package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sharmaumang.hospital_bed_trackker.R;
import com.sharmaumang.hospital_bed_trackker.adapter.bedAdapter;
import com.sharmaumang.hospital_bed_trackker.model.bedModel;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference reference;
    ArrayList<bedModel> list;
    bedAdapter adapter;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedPref = getSharedPreferences("LASTSTATE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("STATE","HOME" );
        editor.apply();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        //TODO : Will maintain the recycler view here

        recyclerView=findViewById(R.id.carList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Hospital Data...");
        progressDialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<bedModel>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {


                    String updateddate= dataSnapshot1.child("Data-Time").getValue().toString();
                    String district= dataSnapshot1.child("District").getValue().toString();
                    String postalcode= dataSnapshot1.child("Postal Code").getValue().toString();
                    String district_postal = district +","+ postalcode;


                    String name=dataSnapshot1.child("Hospital Name").getValue().toString();
                        String avail=dataSnapshot1.child("Number of beds available").getValue().toString();
                        String total=dataSnapshot1.child("Total Number of beds").getValue().toString();

                        bedModel details=new bedModel(name,avail,total,district_postal,updateddate);
                        list.add(details);



                }
                adapter = new bedAdapter(Home.this,list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, Welcome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_LONG).show();
                return true;
            case R.id.aboutDevelopers:
                Toast.makeText(getApplicationContext(),"About developers : Update further",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}