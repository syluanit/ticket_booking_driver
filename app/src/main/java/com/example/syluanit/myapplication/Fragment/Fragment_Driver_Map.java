package com.example.syluanit.myapplication.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syluanit.myapplication.Model.Tracking;
import com.example.syluanit.myapplication.Model.User;
import com.example.syluanit.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment_Driver_Map extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback {
    View view;
    private MapView mapView;
    private GoogleMap mMap;
    private String email;
    private Double lat, lng;
    private Switch SOS;
    private Button btn_user_nearby;
    private HashMap markerList;
    //irebase
    DatabaseReference onlineRef, currentUserRef, counterRef, locations;

    //Location
    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICE_RES_REQUEST = 7172;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Boolean LocationPermissionsGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 5000;
    private static final float DEFAULT_ZOOM = 15f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.drive_map, container, false);

        //irebase
        locations = FirebaseDatabase.getInstance().getReference("Locations");
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");

//        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(mFirebaseUser != null) {
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()); //Do what you need to do with the id
//        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        }
        else {
            LocationPermissionsGranted = true;
            if (checkPlayServices())
            {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }

        setupSystem();

        //tracking
        mapView = (MapView) view.findViewById(R.id.myMap);
        btn_user_nearby = (Button) view.findViewById(R.id.btnUserNearby);
        SOS = (Switch) view.findViewById(R.id.SwitchSOS);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        return view;
    }

    private void loadLocationThisUser(String email) {
        Query user_location = locations.orderByChild("email").equalTo(email);

        user_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren())
                {
                    mMap.clear();
                    markerList = new HashMap();
                    Tracking tracking = postsnapshot.getValue(Tracking.class);

                    LatLng friendLocation = new LatLng(Double.parseDouble(tracking.getLat()),
                            Double.parseDouble(tracking.getLng()));

                    Location currentUser =  new Location("");
                    currentUser.setLatitude(lat);
                    currentUser.setLongitude(lng);

                    Location friend =  new Location("");
                    friend.setLatitude(Double.parseDouble(tracking.getLat()));
                    friend.setLongitude(Double.parseDouble(tracking.getLng()));

                    distance(currentUser, friend);

                    //add riend maker on map
                    if (tracking.getSOS() == 0 || tracking.getSOS() == 2) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(friendLocation)
                                .title(tracking.getEmail())
//                                .snippet("Distance" + new DecimalFormat("#.#").format(distance(currentUser, friend)))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_bus_drive)));
                        markerList.put(tracking.getEmail() + "",marker);
                    }
                    else if (tracking.getSOS() == 1 )
                    {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(friendLocation)
                                .title(tracking.getEmail())
//                                .snippet("Distance" + new DecimalFormat("#.#").format(distance(currentUser, friend)))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_bus_drive_sos)));
                        markerList.put(tracking.getEmail() + "", marker);
                    }
//                    else {
//
//                    }

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),12.0f));
                }


//                LatLng current = new LatLng(lat, lng);
//                mMap.addMarker(new MarkerOptions().
//                        position(current).
//                        title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}


        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(getContext().getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getContext().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(myIntent);
                    //get gps

                }
            });
            dialog.setNegativeButton(getContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
        else {
            if (LocationPermissionsGranted) {
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

                SOS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                        {
                            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                            String.valueOf(mLastLocation.getLatitude()),
                                            String.valueOf(mLastLocation.getLongitude()),1));
                        }
                        else
                        {
                            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                            String.valueOf(mLastLocation.getLatitude()),
                                            String.valueOf(mLastLocation.getLongitude()),0));
                        }
                    }
                });

                btn_user_nearby.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(email))
                        {
                            loadLocationThisUser(email);
                        }
                    }
                });

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Query user = locations.orderByChild("email").equalTo(marker.getTitle());
                        user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                                    Tracking tracking = postsnapshot.getValue(Tracking.class);

                                if (tracking.getSOS() == 1) {
                                    final Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.dialog_turn_sos_off);
                                    TextView back = (TextView) dialog.findViewById(R.id.back_dialog_sos);
                                    TextView accept = (TextView) dialog.findViewById(R.id.accept_dialog_sos);
                                    TextView email = (TextView) dialog.findViewById(R.id.user_sos);

                                    email.setText(tracking.getEmail());

                                    back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                        }
                                    });

                                    accept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            postsnapshot.child("sos").getRef().setValue(2);
                                            postsnapshot.child("solveUser").getRef()
                                                    .setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    dialog.show();

                                }
                            }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        return false;
                    }
                });

            }
        }
    }

    private void getDeviceLocation() {
        Log.d("Drive_Map", "getDeviceLocation: getting the devices current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            if (LocationPermissionsGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d("Drive_Map", "onComplete: found location!");
                            Location currentlocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude())
                                    , DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d("Drive_Map", "onComplete: current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Drive_Map", "getDeviceLocation: SecurityException:" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.e("Drive map", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!(title == "My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (checkPlayServices())
                    {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
            }
        }
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation !=  null)
        {
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            String.valueOf(mLastLocation.getLatitude()),
                            String.valueOf(mLastLocation.getLongitude()),0));
        }
        else
        {
            Log.d("", "displayLocation: Couldn't get the location");
//            Toast.makeText(getActivity(), "Couldn't get the location", Toast.LENGTH_SHORT).show();
        }

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient =  new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,getActivity(), PLAY_SERVICE_RES_REQUEST).show();
            }
            else {
                Toast.makeText(getActivity(), "This device is not supported", Toast.LENGTH_SHORT).show();
//                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    private void setupSystem() {
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class)){
                    currentUserRef.onDisconnect().removeValue();
                    //Set online user in list
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User (FirebaseAuth.getInstance().getCurrentUser().getEmail(),"Online",
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail()+"77A2-000111"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query set_SOS = locations.orderByChild("sos").equalTo(1);
        set_SOS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Tracking tracking = postSnapShot.getValue(Tracking.class);
                    if (tracking.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        SOS.setChecked(true);
                    }
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query SOS_query = locations.orderByChild("sos").equalTo(2);
        SOS_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    Tracking tracking = postSnapShot.getValue(Tracking.class);
                    if (tracking.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        postSnapShot.child("sos").getRef().setValue(0);
                        SOS.setChecked(false);
                        final Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_sos);
                        TextView accept = (TextView) dialog.findViewById(R.id.accept_dialog_sos1);
                        TextView email = (TextView) dialog.findViewById(R.id.user_sos1);

                        email.setText(tracking.getSolveUser());

                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();

                            }
                        });

                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    Log.d("LOG", "onDataChange: " + user.getEmail() + "is " + user.getStatus());

                    if (!user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && mLastLocation!=null){
                        lat = mLastLocation.getLatitude();
                        lng = mLastLocation.getLongitude();
                        email = user.getEmail();}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_join :
//                counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .setValue(new User (FirebaseAuth.getInstance().getCurrentUser().getEmail(),"Online"));
////                adapter.notifyDataSetChanged();
//                break;
//            case R.id.action_logout:
//                currentUserRef.removeValue();
////                adapter.notifyDataSetChanged();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
//        locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
//                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
//                        String.valueOf(mLastLocation.getLatitude()),
//                        String.valueOf(mLastLocation.getLongitude()),0));
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private double distance(Location currentUser, Location friend){
        double theta = currentUser.getLongitude() - friend.getLongitude();
        double dist = Math.sin(deg2rag(currentUser.getLatitude()))
                *Math.sin(deg2rag(friend.getLatitude()))
                *Math.cos(deg2rag(currentUser.getLatitude()))
                *Math.cos(deg2rag(friend.getLatitude()))
                *Math.cos(deg2rag(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }

    private double rad2deg(double rad) {
        return rad * 180 / Math.PI;
    }

    private double deg2rag(double deg) {
        return deg * Math.PI /180;
    }

}
