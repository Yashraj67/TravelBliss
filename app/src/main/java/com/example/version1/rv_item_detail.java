package com.example.version1;

import android.content.Intent;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;


public class rv_item_detail extends Fragment {

    View view;

    int position;
    String fsq_id;
    ImageView images,mmt,bookingcom,oyo,ola,uber,rapido,zomato,swiggy,star;
    TextView placename,distance,rating,mapdis,address,tv1;
    String dis;
    String latitude,longitude;
    CardView cvMaps;
    ImageSlider ivbackcoll;
    String name;
    CheckBox like;
    CollapsingToolbarLayout collapsingToolbarLayout;
    LottieAnimationView progressLoaderAnimation;
    int wish1;
    public rv_item_detail(int position, String fsq_id, String distance,int wish1) {
        this.position = position;
        this.fsq_id = fsq_id;
        dis =distance;
        this.wish1 = wish1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rv_item_detail, container, false);
        ivbackcoll = view.findViewById(R.id.ivbackcoll);
        zomato = view.findViewById(R.id.zomato);
        like = view.findViewById(R.id.like);
        swiggy = view.findViewById(R.id.swiggy);
        ola = view.findViewById(R.id.ola);
        uber = view.findViewById(R.id.uber);
        rapido = view.findViewById(R.id.rapido);
        cvMaps = view.findViewById(R.id.cvMaps);
        address = view.findViewById(R.id.description);
        mapdis = view.findViewById(R.id.mapdis);
        mmt = view.findViewById(R.id.mmt);
        oyo = view.findViewById(R.id.oyo);
        bookingcom = view.findViewById(R.id.bookingcom);
        images= view.findViewById(R.id.images);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        placename = view.findViewById(R.id.placename);
        distance = view.findViewById(R.id.distance);
        rating = view.findViewById(R.id.rating);
        tv1 = view.findViewById(R.id.tv1);
        progressLoaderAnimation = view.findViewById(R.id.progress_loader_animation);
        progressLoaderAnimation.setAnimation(R.raw.travel);
        progressLoaderAnimation.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this.requireContext());

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = null;
        if (user1 != null) {
            uid = user1.getUid();
        } else {
            uid = account.getId();
        }

        for(int i=0;i<15 ;i++){
            String a = Integer.toString(i);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("User" + uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String fsq_id1 = snapshot.child("wish").child(a).child("fsq_id").getValue(String.class);
                    if(Objects.equals(fsq_id1, fsq_id)){
                        like.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        String finalUid = uid;
        like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){



                    String no = Integer.toString(wish1);
                    mDatabase.child("User" + finalUid).child("wish").child(no).child("fsq_id").setValue(fsq_id);
                    mDatabase.child("User" + finalUid).child("wish").child(no).child("dis").setValue(dis);
                    wish1++;

                    Bundle result = new Bundle();
                    result.putInt("df1", wish1);
                    getParentFragmentManager().setFragmentResult("datafrom1", result);
                    Toast.makeText(getContext(), "Added to Wishlist!", Toast.LENGTH_SHORT).show();
                    tv1.setText("Added to wishlist");
                }else{
                    tv1.setText("Add to wishlist");
                }
            }
        });


        zomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri ="https://www.zomato.com/mobile";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        swiggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri ="https://www.swiggy.com/app";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        ola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri ="https://olawebcdn.com/assets/ola-universal-link.html?landing_page=bk&drop_lat="+latitude+"&drop_lng="+longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri ="https://m.uber.com/?client_id=<mHClirme28wuZEzqPiTuviNhVKd-FTtE>&dropoff[latitude]="+latitude+"&dropoff[longitude]="+longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });



        bookingcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "https://www.booking.com/index.en-gb.html?aid=378266&label=booking-name-IquAp%2AEbiLS6jPVl_he8yQS461499016258%3Apl%3Ata%3Ap1%3Ap22%2C563%2C000%3Aac%3Aap%3Aneg%3Afi%3Atikwd-65526620%3Alp9062116%3Ali%3Adec%3Adm%3Appccp%3DUmFuZG9tSVYkc2RlIyh9YYriJK-Ikd_dLBPOo0BdMww&sid=bdebd91e8d58e4a126df9e97d82f81de&";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        mmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri ="https://www.makemytrip.com/hotels/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        oyo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri ="https://www.oyorooms.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        cvMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q=loc:" + " (" + name + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });



        String url = "https://api.foursquare.com/v3/places/"+fsq_id+"?fields=name%2Cphotos%2Cdistance%2Clocation%2Crating%2Cgeocodes";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String location =  "Location : " + response.getJSONObject("location").getString("address");
                     name = response.getString("name");
                    JSONArray j = response.getJSONArray("photos");
                    ArrayList<SlideModel> imageList = new ArrayList<>();
                    for(int i =0 ; i<j.length() ; i++){
                        String prefix = response.getJSONArray("photos").getJSONObject(i).getString("prefix");
                        String suffix = response.getJSONArray("photos").getJSONObject(i).getString("suffix");
                        String width = response.getJSONArray("photos").getJSONObject(i).getString("width");
                        String height = response.getJSONArray("photos").getJSONObject(i).getString("height");
                        String imgurl = prefix+width+"x"+height+suffix;
                        imageList.add(new SlideModel(imgurl,ScaleTypes.FIT));
                    }
                    ivbackcoll.setImageList(imageList,ScaleTypes.FIT);
                    String prefix = response.getJSONArray("photos").getJSONObject(0).getString("prefix");
                    String suffix = response.getJSONArray("photos").getJSONObject(0).getString("suffix");
                    String rating1  = response.getString("rating");
                    String width = response.getJSONArray("photos").getJSONObject(0).getString("width");
                    String height = response.getJSONArray("photos").getJSONObject(0).getString("height");
                    String imgurl = prefix+width+"x"+height+suffix;
                    latitude = response.getJSONObject("geocodes").getJSONObject("main").getString("latitude");
                    longitude = response.getJSONObject("geocodes").getJSONObject("main").getString("longitude");

                    Picasso.get().load(imgurl).into(images);
                    collapsingToolbarLayout.setTitle(name);
                    placename.setText(name);
                    distance.setText(dis);
                    rating.setText(rating1);
                    mapdis.setText(dis);
                    address.setText(location);
                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "fsq3BqtJCI1HfYU26sEaMsqSrWttMl4x4zuvNWZk0rY4kaU=");

                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                delete();
            }
        });
        return view;
    }
    public void delete()
    {

        FragmentManager fm=getParentFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }

        ft.commit();
    }

    private void SetFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFrag,fragment);
        fragmentTransaction.commit();
    }

}