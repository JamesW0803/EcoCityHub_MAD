package com.example.madassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

public class ApplicantsList extends AppCompatActivity {

    Spinner statusSpinner1, statusSpinner2, statusSpinner3, statusSpinner4, statusSpinner5;
    MaterialButton BTDashboardBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants_list);
        statusSpinner1 = findViewById(R.id.statusSpinner1);
        statusSpinner2 = findViewById(R.id.statusSpinner2);
        statusSpinner3 = findViewById(R.id.statusSpinner3);
        statusSpinner4 = findViewById(R.id.statusSpinner4);
        statusSpinner5 = findViewById(R.id.statusSpinner5);
        List <String> mList = Arrays.asList("Pending", "Accepted", "Rejected");

        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, mList);
        mArrayAdapter.setDropDownViewResource(R.layout.spinner_list);

        statusSpinner1.setAdapter(mArrayAdapter);
        statusSpinner2.setAdapter(mArrayAdapter);
        statusSpinner3.setAdapter(mArrayAdapter);
        statusSpinner4.setAdapter(mArrayAdapter);
        statusSpinner5.setAdapter(mArrayAdapter);

        BTDashboardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicantsList.this, VolunteerActivityPost.class);
                startActivity(intent);
            }
        });
    }
}