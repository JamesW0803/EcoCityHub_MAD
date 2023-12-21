package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrganizerDashboard extends AppCompatActivity {
// testing use
    ArrayList <ActivityHelper> activity_list;
    MaterialButton BTDashboardBack;
    RecyclerView recyclerView;
    public final static String ACTIVITY_KEY = "ACTIVITY KEY";
    ActivityAdapter activityAdapter;
    FloatingActionButton FOBAddActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);
        BTDashboardBack = findViewById(R.id.BTDashboardBack);

        FOBAddActivities = findViewById(R.id.FOBAddActivities);

        FOBAddActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerDashboard.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        String activityKey = getIntent().getStringExtra("activityKey");

        recyclerView = findViewById(R.id.activity_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activity_list = new ArrayList<ActivityHelper>();

        activityAdapter = new ActivityAdapter(activity_list, key -> {
            Intent intent = new Intent(OrganizerDashboard.this, VolunteerActivityPost.class);
            intent.putExtra(ACTIVITY_KEY, key);
            startActivity(intent);
        });
        recyclerView.setAdapter(activityAdapter);

        DatabaseReference activitiesRef = FirebaseDatabase.getInstance().getReference("Activities");

        activitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activity_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CreateActivityHelper activity = dataSnapshot.getValue(CreateActivityHelper.class);
                    String key = dataSnapshot.getKey();
                    if (activity != null) {
                        ActivityHelper activityHelper = new ActivityHelper(activity.getTitle(), activity.getLocation(), activity.getDateActivity(), key);
                        activityHelper.setStartTime(activity.getStartTimeActivity());
                        activity_list.add(activityHelper);
                    }
                }
                activityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BTDashboardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerDashboard.this, OrganizerMainPage.class);
                intent.putExtra("activityKey", activityKey);
                startActivity(intent);
            }
        });
    }
}