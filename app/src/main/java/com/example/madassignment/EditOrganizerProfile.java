package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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
    DatabaseReference reference;
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

        BTEditOrgProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditOrganizerProfile.this, OrganizerProfile.class);
                intent.putExtra("activityKey", activityKey);
                startActivity(intent);
            }
        });

        activityKey = getIntent().getStringExtra("activityKey");
        if (activityKey != null) {
            reference = FirebaseDatabase.getInstance().getReference("Organizer").child(activityKey);
        } else {
            Toast.makeText(this, "Error getting the database access key", Toast.LENGTH_SHORT).show();
        }

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

                String organizerID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                SignUpOrganizerHelper updatedOrganizer = new SignUpOrganizerHelper(username, bio, gender, dateOfBirth, contactNo, email, address, password);
                reference.setValue(updatedOrganizer);

                Toast.makeText(EditOrganizerProfile.this, "You have successfully edit your profile! ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditOrganizerProfile.this, OrganizerProfile.class);
                startActivity(intent);
            }
        });
    }
}