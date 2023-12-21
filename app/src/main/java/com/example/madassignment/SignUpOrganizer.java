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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SignUpOrganizer extends AppCompatActivity {
    AutoCompleteTextView genderMenu;
    TextInputEditText usernameEditText, bioEditText, EditTextDOB, phoneEditText, emailEditText, addressEditText, passwordEditText;
    AppCompatButton OrgSignUpBT;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_organizer);
        genderMenu = findViewById(R.id.genderMenu);
        usernameEditText = findViewById(R.id.usernameEditText);
        bioEditText = findViewById(R.id.bioEditText);
        EditTextDOB = findViewById(R.id.EditTextDOB);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        OrgSignUpBT = findViewById(R.id.OrgSignUpBT);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpOrganizer.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        EditTextDOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }}, year, month, day);
                    datePickerDialog.show();
            }
        });

        OrgSignUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Organizer");

                String activityKey = reference.push().getKey();

                String username, bio, gender, dateOfBirth, contactNo, email, address, password;
                username = usernameEditText.getText().toString();
                bio = bioEditText.getText().toString();
                gender = genderMenu.getText().toString();
                dateOfBirth = EditTextDOB.getText().toString();
                contactNo = phoneEditText.getText().toString();
                email = emailEditText.getText().toString();
                address = addressEditText.getText().toString();
                password = passwordEditText.getText().toString();

                checkUserExists(username, email, contactNo, new FirebaseOperationCallback() {
                    @Override
                    public void onSuccess(boolean result) {
                        if (!result) {
                            SignUpOrganizerHelper organizerHelper = new SignUpOrganizerHelper(username, bio, gender, dateOfBirth, contactNo, email, address, password);
                            reference.child(activityKey).setValue(organizerHelper);

                            Toast.makeText(SignUpOrganizer.this, "You have successfully signed up as an Organizer!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpOrganizer.this, EditOrganizerProfile.class);
                            intent.putExtra("activityKey", activityKey);

                            Intent intent2 = new Intent(SignUpOrganizer.this, OrganizerProfile.class);
                            intent2.putExtra("activityKey", activityKey);

                            Intent intent3 = new Intent(SignUpOrganizer.this, OrganizerMainPage.class);
                            intent3.putExtra("activityKey", activityKey);
                            startActivity(intent3);
                        }
                    }
                });
            }
        });

    }

    private void checkUserExists(String username, String email, String phone, FirebaseOperationCallback callback) {
        Query usernameRef = reference.orderByChild("username").equalTo(username);

        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SignUpOrganizer.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                    callback.onSuccess(true);
                } else {
                    checkEmailExists(email, phone, callback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onSuccess(false);
            }
        });
    }

    private void checkEmailExists(String email, String phone, FirebaseOperationCallback callback) {
        Query emailRef = reference.orderByChild("email").equalTo(email);

        emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SignUpOrganizer.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                    callback.onSuccess(true);
                } else {
                    checkPhoneExists(phone, callback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onSuccess(false);
            }
        });
    }

    private void checkPhoneExists(String phone, FirebaseOperationCallback callback) {
        Query phoneRef = reference.orderByChild("contactNo").equalTo(phone);

        phoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SignUpOrganizer.this, "Phone number already exists!", Toast.LENGTH_SHORT).show();
                    callback.onSuccess(true);
                } else {
                    callback.onSuccess(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onSuccess(false);
            }
        });
    }
}