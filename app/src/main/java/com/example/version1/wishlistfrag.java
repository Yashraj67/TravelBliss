package com.example.version1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class wishlistfrag extends Fragment implements rv_interface{

    View view;
    ListView list;
    ArrayList<TripData> arr = new ArrayList<>();
    Context context = getContext();
    ArrayList<String> wish = new ArrayList<>();
    LottieAnimationView progressLoaderAnimation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wishlistfrag, container, false);
        list = view.findViewById(R.id.listview);
        progressLoaderAnimation = view.findViewById(R.id.progress_loader_animation);
        progressLoaderAnimation.setAnimation(R.raw.travel);
        progressLoaderAnimation.setVisibility(View.VISIBLE);
        CustomBaseAdapter adapter = new CustomBaseAdapter(view.getContext().getApplicationContext(), arr);
        final int[] s1 = new int[1];
        getParentFragmentManager().setFragmentResultListener("datafrom1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                 s1[0] = result.getInt("df1");
                Log.i("df1", String.valueOf(s1[0]));
            }
        });

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        String uid = null;
        if (user1 != null) {
            uid = user1.getUid();
        } else {
            assert account != null;
            uid = account.getId();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User" + uid);


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(getContext()).setTitle("Do you want to remove "+ arr.get(position).getTitle() +" from wishlist?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String fsq_id = arr.get(position).getFsq_id();
                                arr.remove(position);
                                int v = 0;
                                while(v!=15){

                                    int finalV = v;
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String fs = snapshot.child("wish").child(String.valueOf(finalV)).child("fsq_id").getValue(String.class);
                                            if(Objects.equals(fs, fsq_id)){
                                                databaseReference.child("wish").child(String.valueOf(finalV)).removeValue();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    v=v+1;

                                }
                                adapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnItemClicked(position);
            }
        });

        int k = 15;
        final int[] i = {0};
        final int[] flag = {0};

        while(k!=0) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int x = i[0];
                    i[0] = i[0] + 1;
                    String no = Integer.toString(x);
                    String fsq_id = snapshot.child("wish").child(no).child("fsq_id").getValue(String.class);
                    String dis = snapshot.child("wish").child(no).child("dis").getValue(String.class);
                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    int flag1=0;
                    for(int i=0;i<wish.size();i++){

                        if(Objects.equals(wish.get(i), fsq_id)){
                            flag1 = 1;
                            break;
                        }
                    }
                    if(flag1==0){
                        wish.add(fsq_id);
                        String url = "https://api.foursquare.com/v3/places/" + fsq_id + "?fields=name%2Cphotos%2Cdistance%2Clocation%2Crating%2Cgeocodes";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String name = response.getString("name");
                                    String prefix = response.getJSONArray("photos").getJSONObject(0).getString("prefix");
                                    String suffix = response.getJSONArray("photos").getJSONObject(0).getString("suffix");
                                    String rating1 = response.getString("rating");
                                    String location = null;
                                    String width = response.getJSONArray("photos").getJSONObject(0).getString("width");
                                    String height = response.getJSONArray("photos").getJSONObject(0).getString("height");
                                    String imgurl = prefix + width + "x" + height + suffix;
                                    arr.add(new TripData(imgurl, name, rating1, dis, location, fsq_id));
                                    list.setAdapter(adapter);
                                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    flag[0] =1;
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressLoaderAnimation.setVisibility(View.INVISIBLE);

                                flag[0] =1;
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Accept", "application/json");
                                params.put("Authorization", "fsq3BqtJCI1HfYU26sEaMsqSrWttMl4x4zuvNWZk0rY4kaU=");

                                return params;
                            }
                        };
                        requestQueue.add(jsonObjectRequest);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    flag[0]=1;
                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Your Wishlist is Empty!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();

                }
            });
            if(flag[0] ==1) break;
            k=k-1;
        }











        return view;
    }

    @Override
    public void OnItemClicked(int position) {
        String fsq_id1 = arr.get(position).getFsq_id();
        String distance = arr.get(position).getDistance();
        addFragment(new rv_item_detail(position,fsq_id1,distance,0));
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flFrag, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }
}