package com.example.tushauriane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class ContactActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText testimonial;
    EditText username;
    EditText email;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        email = findViewById(R.id.emailContact);
        username = findViewById(R.id.nameContact);
        testimonial = findViewById(R.id.testimonial);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        AppCompatButton Submit = findViewById(R.id.btnSubmit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToStorage();
            }
        });
    }

    private void uploadDataToStorage(){

            String testimonials = testimonial.getText().toString().trim();
            String usernames = username.getText().toString().trim();
            String emails = email.getText().toString().trim();

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String currentUserId = currentUser.getUid();


                JSONObject bookingData = new JSONObject();
                try {
                    bookingData.put("Username", usernames);
                    bookingData.put("Email_address", emails);
                    bookingData.put("Testimonial", testimonials);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Generate a unique ID for the booking
                String bookingId = UUID.randomUUID().toString();

                // Upload the data to Firebase Storage
                StorageReference fileRef = storageRef.child("testimonials/" + bookingId + ".json");
                fileRef.putBytes(bookingData.toString().getBytes())
                        .addOnSuccessListener(taskSnapshot -> {
                            Log.d("Firebase Storage", "File uploaded successfully");
                            // Handle success
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firebase Storage", "Error uploading file", e);
                            // Handle failure
                        });
            }
        }
    }
