package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizerProfile extends AppCompatActivity {
    TextView TVUsername, TVBio;
    AppCompatButton BTEditOrgProfile;
    MaterialButton BTProfileBack;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_profile);
        TVUsername = findViewById(R.id.TVUsername);
        TVBio = findViewById(R.id.TVBio);
        BTEditOrgProfile = findViewById(R.id.BTEditOrgProfile);
        BTProfileBack = findViewById(R.id.BTProfileBack);

        String username = getIntent().getStringExtra("username");

        if (username != null){
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference().child("Organizer").child(username);

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String username = snapshot.child("username").getValue(String.class);
                        String bio = snapshot.child("bio").getValue(String.class);
                        TVUsername.setText(username);
                        TVBio.setText(bio);
                    }else{
                        Toast.makeText(OrganizerProfile.this, "The Organizer profile doesn't exist. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Read Error", error.toString());
                }
            });
        }else{
            Toast.makeText(OrganizerProfile.this, "Invalid activity key. Unable to load profile.", Toast.LENGTH_SHORT).show();
        }

        BTEditOrgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditOrganizerProfile.class);
                intent.putExtra("activityKey", username);
                startActivity(intent);
            }
        });

        BTProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerProfile.this, OrganizerMainPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}