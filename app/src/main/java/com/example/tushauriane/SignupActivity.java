package com.example.tushauriane;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase db;

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null) {
            currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                currentUser.reload();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();


        EditText usernameInput = findViewById(R.id.Username);
        EditText emailInput = findViewById(R.id.Email);
        EditText passwordInput = findViewById(R.id.Password);
        AppCompatButton btnSignup = findViewById(R.id.signupBtn);


        btnSignup.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignupActivity.this, "Email is required", Toast.LENGTH_LONG).show();
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Please enter a valid email address");
                return;
            }


            if (TextUtils.isEmpty(username)) {
                Toast.makeText(SignupActivity.this, "Username is required", Toast.LENGTH_LONG).show();
                return;
            }

            if (username.length() < 3) {
                Toast.makeText(SignupActivity.this, "Username should be at least three characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignupActivity.this, "Password is required", Toast.LENGTH_LONG).show();
                return;
            }
            if (password.length() < 6) {
                passwordInput.setError("Password should not be less than six characters");
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Successfully created an account.Please verify your email.", Toast.LENGTH_LONG).show();

                                // Get the current user's UID
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String uid = currentUser.getUid();

                                // Write the user details to the Realtime Database
                                if (!email.isEmpty()) {
                                    ReadWriteUserDetails users = new ReadWriteUserDetails(email, username);
                                    db = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = db.getReference("Registered Users");

                                    reference.child(uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this, "Successfully updated", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }

                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignupActivity.this, "User registration failed.Try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

    }
}