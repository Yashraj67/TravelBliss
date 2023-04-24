package com.example.version1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.browser.trusted.sharing.ShareTarget;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class homefrag extends Fragment implements rv_interface{
    View view;
    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<TripData> data;
    ImageView beach,hotels,hills,explore;
    ImageView beachl,hotelsl,hillsl,explorel,addwish;
    SearchView searchView;
    public double latitude;
    public double longitude;
    public int wish = 0;
    LottieAnimationView progressLoaderAnimation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_homefrag, container, false);


        searchView = view.findViewById(R.id.sv);
        beach = view.findViewById(R.id.beach);
        hotels = view.findViewById(R.id.metro);
        hills = view.findViewById(R.id.forest);
        explore = view.findViewById(R.id.explore);
        beachl = view.findViewById(R.id.beachl);
        hotelsl = view.findViewById(R.id.metrol);

        hillsl = view.findViewById(R.id.forestl);
        explorel = view.findViewById(R.id.explorel);
        progressLoaderAnimation = view.findViewById(R.id.progress_loader_animation);
        progressLoaderAnimation.setAnimation(R.raw.travel);
        progressLoaderAnimation.setVisibility(View.VISIBLE);
        initData("all",0);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressLoaderAnimation.setVisibility(View.VISIBLE);
                initData(query,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressLoaderAnimation.setVisibility(View.VISIBLE);
                initData(newText,1);
                return false;
            }
        });



        beach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                progressLoaderAnimation.setVisibility(View.VISIBLE);
                initData("beach",0);

                hotelsl.setVisibility(View.INVISIBLE);
                hillsl.setVisibility(View.INVISIBLE);
                explorel.setVisibility(View.INVISIBLE);
                beachl.setVisibility(View.VISIBLE);
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                progressLoaderAnimation.setVisibility(View.VISIBLE);
                initData("all",0);
                hotelsl.setVisibility(View.INVISIBLE);
                hillsl.setVisibility(View.INVISIBLE);
                explorel.setVisibility(View.VISIBLE);
                beachl.setVisibility(View.INVISIBLE);
            }
        });
        hills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                progressLoaderAnimation.setVisibility(View.VISIBLE);
                initData("hills",0);
                hotelsl.setVisibility(View.INVISIBLE);
                hillsl.setVisibility(View.VISIBLE);
                explorel.setVisibility(View.INVISIBLE);
                beachl.setVisibility(View.INVISIBLE);
            }
        });
        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.INVISIBLE);
                progressLoaderAnimation.setVisibility(View.VISIBLE);
                initData("hotels",0);
                hotelsl.setVisibility(View.VISIBLE);
                hillsl.setVisibility(View.INVISIBLE);
                explorel.setVisibility(View.INVISIBLE);
                beachl.setVisibility(View.INVISIBLE);
            }
        });




        return view;
    }
    private void initData(String query,int f) {
        data = new ArrayList<>();

        getLocation();
        String lat = String.valueOf(latitude);
        String log = String.valueOf(longitude);
        RequestQueue requestQueue = Volley.newRequestQueue(this.requireContext());
        String CLIENT_ID = "132XB34C0QPPRZKD3JFFJY5LTYARZQBFDLMVVAUQI0CJLKPW";
        String CLIENT_PASSWORD  = "4ZJPMWLUHU1C55O0J2ZQRREVVISYT2N3DSR35NRIEDKNEWMQ";
        String url = null;
        if(f==1){
            url = "https://api.foursquare.com/v3/places/search?query="+query+"&ll="+lat+"%2C"+log+"&radius=100000&categories=16000%2C10000%2C13000%2C16003%2C16015%2C16019%2C16027&fields=name%2Crating%2Clocation%2Cphotos%2Cdistance%2Cfsq_id&limit=50";

        }
        else{
            if (Objects.equals(query, "all")) {
                url = "https://api.foursquare.com/v3/places/search?ll=" + lat + "%2C" + log + "&radius=100000&categories=16000%2C10000%2C13000%2C16003%2C16015%2C16019%2C16027&fields=name%2Crating%2Clocation%2Cphotos%2Cdistance%2Cfsq_id&limit=50";

            } else if (Objects.equals(query, "beach")) {
                url = "https://api.foursquare.com/v3/places/search?ll=" + lat + "%2C" + log + "&radius=100000&categories=16003&fields=name%2Crating%2Clocation%2Cphotos%2Cdistance%2Cfsq_id&limit=50";

            } else if (Objects.equals(query, "hills")) {
                url = "https://api.foursquare.com/v3/places/search?ll=" + lat + "%2C" + log + "&radius=100000&categories=16015%2C16019%2C16027&fields=name%2Crating%2Clocation%2Cphotos%2Cdistance%2Cfsq_id&limit=50";

            } else if (Objects.equals(query, "hotels")) {
                url = "https://api.foursquare.com/v3/places/search?ll=" + lat + "%2C" + log + "&radius=100000&categories=13000&fields=name%2Crating%2Clocation%2Cphotos%2Cdistance%2Cfsq_id&limit=50";

            }
        }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("apicall", response.getJSONArray("results").getJSONObject(3).getString("name"));
                        } catch (JSONException e) {
                            Log.d("apie",e.toString());
                        }
                        try {
                            JSONArray j1 = response.getJSONArray("results");
                            for(int i = 0 ; i< j1.length() ; i++){
                                JSONObject j2 = j1.getJSONObject(i);

                                try{
                                    String location1 = "Location";
                                    location1 = j2.getJSONObject("location").getString("address");
                                    String Name = j2.getString("name");
                                    String dis = j2.getString("distance");
                                    String fsq_id = j2.getString("fsq_id");
                                    String rating = j2.getString("rating");
                                    JSONArray p = j2.getJSONArray("photos");
                                    JSONObject p1 = p.getJSONObject(0);
                                    String prefix = p1.getString("prefix");
                                    String suffix = p1.getString("suffix");
                                    String width = p1.getString("width");
                                    String height = p1.getString("height");

//                                    String prefix = null;
//                                    String suffix = null;
                                    int d = Integer.parseInt(dis);
                                    double d1= d/1000;
                                    dis= Double.toString(d1);
                                    location1 = "Location : "+location1;
                                    data.add(new TripData(prefix+width+"x"+height+suffix,Name,rating,dis + " Km",location1,fsq_id));
                                    initRecyclerView();
                                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                }catch(JSONException p){
                                    progressLoaderAnimation.setVisibility(View.INVISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);
//                                    Toast.makeText(getContext(), p.toString(), Toast.LENGTH_SHORT).show();
                                    continue;

                                }
                            }
                        } catch (JSONException e) {
                            progressLoaderAnimation.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d("error1",error.toString());
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


    }

    private void initRecyclerView() {

        recyclerView = view.findViewById(R.id.rvData);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(data,getContext(),this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getLocation(){
        GpsTracker gpsTracker = new GpsTracker(getContext());
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void OnItemClicked(int position) {
        String fsq_id1 = data.get(position).getFsq_id();
        String distance = data.get(position).getDistance();
        addFragment(new rv_item_detail(position,fsq_id1,distance,wish));


                getParentFragmentManager().setFragmentResultListener("datafrom1", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        wish = result.getInt("df1");
                    }
                });


    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flFrag, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }


    class GpsTracker extends Service implements LocationListener {
        private Context mContext = getContext();

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GpsTracker(Context context) {

            this.mContext = context;
            getLocation();
        }

        @SuppressLint("MissingPermission")
        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        //check the network permission
                        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }

                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            //check the network permission
                            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                            }
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         * */

        @SuppressLint("MissingPermission")
        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(GpsTracker.this);
            }
        }

        /**
         * Function to get latitude
         * */

        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */

        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */

        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         * */

        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }

        @Override
        public void onLocationChanged(Location location) {
            latitude = getLatitude();
            longitude = getLongitude();

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }
    }
}