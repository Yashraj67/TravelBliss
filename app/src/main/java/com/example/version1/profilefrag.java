package com.example.version1;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaParser;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;


public class profilefrag extends Fragment {

    View view;
    TextView username,password,email ,tv13,tv9;
    Button Logout;

    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    String uid;

    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    LottieAnimationView progressLoaderAnimation;
    FloatingActionButton fbt;
    ImageView imageView;
    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profilefrag, container, false);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password1);
        email = view.findViewById(R.id.email);
        tv9 = view.findViewById(R.id.textView9);
        Logout = view.findViewById(R.id.button_logout);
        tv13 = view.findViewById(R.id.textView13);
        fbt = view.findViewById(R.id.float_btn);
        imageView = view.findViewById(R.id.profile_img);
        progressLoaderAnimation = view.findViewById(R.id.progress_loader_animation);
        progressLoaderAnimation.setAnimation(R.raw.travel);
        progressLoaderAnimation.setVisibility(View.VISIBLE);
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AppInfo.class);
                startActivity(intent);
            }
        });





        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),MainActivity.class);
                Toast.makeText(getContext(), "Logout Successful!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                getActivity().finish();
            }
        });


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(getContext(),gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account!=null){
            uid = account.getId();
            String user = account.getDisplayName();
            String em = account.getEmail();
            username.setText(user);
            password.setVisibility(View.VISIBLE);
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            password.setText("Not Available!");
            email.setText(em);
            tv13.setText(user);

        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if(user1!=null){
            uid = user1.getUid();
            DatabaseReference databaseReference = database.getReference("User"+uid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    String em = snapshot.child("Email").getValue(String.class);
                    String pass = snapshot.child("Password").getValue(String.class);
                    String user = snapshot.child("Username").getValue(String.class);
                    username.setText(user);
                    password.setText(pass);
                    email.setText(em);
                    tv13.setText(user);
                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        StorageReference profilePicRef = storageRef.child("profilePictures/" + uid + ".jpg");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Load the profile picture into the ImageView

                Picasso.get().load(uri).into(imageView);
            }
        });

        fbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressLoaderAnimation.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });





        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            StorageReference profilePicRef = storageRef.child("profilePictures/" + uid + ".jpg");
            UploadTask uploadTask = profilePicRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Profile picture uploaded successfully
                    Glide.with(getContext())
                            .load(selectedImageUri)
                            .into(imageView);
                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Profile picture added successfully!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Profile picture upload failed
                    Toast.makeText(getContext(), "Failed to set profile picture!", Toast.LENGTH_SHORT).show();
                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                }
            });
        }
    }


}