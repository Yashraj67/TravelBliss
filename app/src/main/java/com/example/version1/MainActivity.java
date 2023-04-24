package com.example.version1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 22;
    private TextView SignUp ;
    private EditText Username;
    private EditText Password;
    private FirebaseAuth mAuth;
    private Button login;
    private ImageView google ,facebook;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

//    @Override
//    public void onStart(){
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser!=null){
//            Intent myIntent = new Intent(MainActivity.this, HomePage.class);
//            startActivity(myIntent);
//        }
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.action));

        mAuth = FirebaseAuth.getInstance();

        SignUp = findViewById(R.id.tvSignUp);
        Username = findViewById(R.id.etUsername);
        Password = findViewById(R.id.etPassword);
        login = findViewById(R.id.btnLogin);
        google = findViewById(R.id.btnGoogle);

       gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gSignIn();

            }
        });


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Signup.class);
                startActivity(myIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Username.getText().toString();
                String password = Password.getText().toString();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(MainActivity.this, "Invalid Cridentinals !", Toast.LENGTH_SHORT).show();
                }else{
                    signIn(email,password);
                }


            }
        });

    }

    private void gSignIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                HomeActivity();
                // Signed in successfully, show authenticated UI.
                // You can get the user's ID, display name, email address, and profile picture using the account object.
            } catch (ApiException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                // The ApiException status code indicates the sign-in attempt failed.
                // You can get more information from the ApiException object.
            }
        }
    }


    private void HomeActivity() {

        Intent intent = new Intent(MainActivity.this,HomePage.class);
        startActivity(intent);
        finish();
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("abc", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent myIntent = new Intent(MainActivity.this, HomePage.class);
                            Username.getText().clear();
                            Password.getText().clear();
                            startActivity(myIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failed", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END sign_in_with_email]
    }
}