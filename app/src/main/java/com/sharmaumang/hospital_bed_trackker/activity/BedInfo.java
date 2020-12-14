package com.sharmaumang.hospital_bed_trackker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.sharmaumang.hospital_bed_trackker.R;

public class BedInfo extends AppCompatActivity {

    TextInputEditText mTotalBeds, mAvailableBeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_info);

        mTotalBeds = findViewById(R.id.et_number);
        mAvailableBeds = findViewById(R.id.et_model);




    }
}