package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditOrganizerProfile extends AppCompatActivity {

    AutoCompleteTextView genderMenu;
    TextInputEditText usernameEditText, bioEditText, EditTextDOB, phoneEditText, emailEditText, addressEditText, passwordEditText;
    AppCompatButton BTUpdateOrgProfile;
    MaterialButton BTEditOrgProfileBack;
    String activityKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_organizer_profile);
        genderMenu = findViewById(R.id.genderMenu);
        usernameEditText = findViewById(R.id.usernameEditText);
        bioEditText = findViewById(R.id.bioEditText);
        EditTextDOB = findViewById(R.id.EditTextDOB);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        BTUpdateOrgProfile = findViewById(R.id.BTUpdateOrgProfile);
        BTEditOrgProfileBack = findViewById(R.id.BTEditOrgProfileBack);

        List<String> gender = Arrays.asList("Male", "Female", "Prefer not to say");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, gender);
        genderMenu.setAdapter(adapter);

        EditTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditOrganizerProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                EditTextDOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }}, year, month, day);
                datePickerDialog.show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        BTEditOrgProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditOrganizerProfile.this, OrganizerProfile.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        if (username.isEmpty()){
            Toast.makeText(this, "No username found. Please log in again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LogInOrganizer.class);
            startActivity(intent);
            finish();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Organizer").child(username);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SignUpOrganizerHelper organizerHelper = snapshot.getValue(SignUpOrganizerHelper.class);
                if (organizerHelper != null){
                    genderMenu.setText(organizerHelper.getGender());
                    usernameEditText.setText(organizerHelper.getUsername());
                    bioEditText.setText(organizerHelper.getBio());
                    EditTextDOB.setText(organizerHelper.getDateOfBirth());
                    phoneEditText.setText(organizerHelper.getContactNo());
                    emailEditText.setText(organizerHelper.getEmail());
                    addressEditText.setText(organizerHelper.getAddress());
                    passwordEditText.setText(organizerHelper.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditOrganizerProfile.this, "Failed to load profile details.", Toast.LENGTH_SHORT).show();
            }
        });

        BTUpdateOrgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, bio, gender, dateOfBirth, contactNo, email, address, password;
                username = usernameEditText.getText().toString();
                bio = bioEditText.getText().toString();
                gender = genderMenu.getText().toString();
                dateOfBirth = EditTextDOB.getText().toString();
                contactNo = phoneEditText.getText().toString();
                email = emailEditText.getText().toString();
                address = addressEditText.getText().toString();
                password = passwordEditText.getText().toString();

                SignUpOrganizerHelper updatedOrganizer = new SignUpOrganizerHelper(username, bio, gender, dateOfBirth, contactNo, email, address, password);
                reference.setValue(updatedOrganizer).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditOrganizerProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditOrganizerProfile.this, OrganizerProfile.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(EditOrganizerProfile.this, "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}