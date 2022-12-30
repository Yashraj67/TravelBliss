package com.example.travelbliss;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class signup extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private EditText mail;
    private EditText dob;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    private Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        signup = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        Username = findViewById(R.id.etUsername);
        Password = findViewById(R.id.etPassword);
        mail = findViewById(R.id.etEmail);
        dob = findViewById(R.id.etDOB);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Username.getText().toString();
                String email = mail.getText().toString();
                String password = Password.getText().toString();
                String DOB = dob.getText().toString();
                mDatabase = database.getInstance().getReference();
                createAccount(email,password);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                mDatabase.child("User"+uid).child("Username").setValue(username);
                mDatabase.child("User"+uid).child("Email").setValue(email);
                mDatabase.child("User"+uid).child("Password").setValue(password);
                mDatabase.child("User"+uid).child("DOB").setValue(DOB);


            }
        });
    }
    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent myIntent = new Intent(signup.this,MainActivity.class );
                            Toast.makeText(signup.this, "Authentication Succesful.",
                                    Toast.LENGTH_SHORT).show();
                            Username.getText().clear();
                            Password.getText().clear();
                            mail.getText().clear();
                            dob.getText().clear();

                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END create_user_with_email]
    }
}