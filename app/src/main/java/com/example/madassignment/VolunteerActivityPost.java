package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VolunteerActivityPost extends AppCompatActivity {
    MaterialButton BTActivityBack, BTEditActivity;
    TextView TVActivityName, TVActivityDesc, TVPoints, TVDateActivity, TVTimeActivity, TVLocationActivity, TVAddressActivity, TVAgeGroup, TVRequirements, TVPhoneNumber;
    AppCompatButton BTApplications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_post);
        BTActivityBack = findViewById(R.id.BTActivityBack);
        BTEditActivity = findViewById(R.id.BTEditActivity);
        TVActivityName = findViewById(R.id.TVActivityName);
        TVActivityDesc = findViewById(R.id.TVActivityDesc);
        TVPoints = findViewById(R.id.TVPoints);
        TVDateActivity = findViewById(R.id.TVDateActivity);
        TVTimeActivity = findViewById(R.id.TVTimeActivity);
        TVLocationActivity = findViewById(R.id.TVLocationActivity);
        TVAddressActivity = findViewById(R.id.TVAddressActivity);
        TVAgeGroup = findViewById(R.id.TVAgeGroup);
        TVRequirements = findViewById(R.id.TVRequirements);
        TVPhoneNumber = findViewById(R.id.TVPhoneNumber);
        BTApplications = findViewById(R.id.BTApplications);

        String activityKey = getIntent().getStringExtra(OrganizerDashboard.ACTIVITY_KEY);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities").child(activityKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ActivityHelperClass activityHelper = snapshot.getValue(ActivityHelperClass.class);

                if (activityHelper != null){
                    TVActivityName.setText(activityHelper.getTitle());
                    TVActivityDesc.setText(activityHelper.getDescription());
                    TVPoints.setText(activityHelper.getPoints() + " Points");
                    TVDateActivity.setText(activityHelper.getDateActivity());
                    TVTimeActivity.setText(activityHelper.getStartTimeActivity() + " - " + activityHelper.getEndTimeActivity());
                    TVLocationActivity.setText(activityHelper.getLocation());
                    TVAddressActivity.setText(activityHelper.getAddress());
                    TVAgeGroup.setText(activityHelper.getMinimumAge() + " - " + activityHelper.getMaximumAge() + " years old");
                    TVRequirements.setText(activityHelper.getRequirements());
                    TVPhoneNumber.setText(activityHelper.getContactNo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BTActivityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VolunteerActivityPost.this, OrganizerDashboard.class);
                startActivity(intent);
            }
        });

        BTEditActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("activityKey", activityKey);
                startActivity(intent);
            }
        });

        BTApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VolunteerActivityPost.this, ApplicantsList.class);
                startActivity(intent);
            }
        });
    }
}