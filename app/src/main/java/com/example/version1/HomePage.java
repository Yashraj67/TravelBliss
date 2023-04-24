package com.example.version1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {


    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    BottomNavigationView bv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        replaceFragment(new homefrag());
        bv1  = findViewById(R.id.bnv1);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            String user1 = account.getId();
            String username = account.getDisplayName();
            String email = account.getEmail();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("User"+user1).child("Username").setValue(username);
            mDatabase.child("User"+user1).child("Email").setValue(email);
        }


        bv1.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new homefrag());
                    break;
                case R.id.wishlist:
                    replaceFragment(new wishlistfrag());
                    break;
                case R.id.profile:
                    replaceFragment(new profilefrag());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.flFrag, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }

    }

    private void addFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flFrag, fragment);
        fragmentTransaction.addToBackStack(backStateName);
        fragmentTransaction.commitAllowingStateLoss();
    }
//    private void SetFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.flFrag,fragment);
//        fragmentTransaction.commit();
//    }
}