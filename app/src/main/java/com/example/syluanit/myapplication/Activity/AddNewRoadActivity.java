package com.example.syluanit.myapplication.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.syluanit.myapplication.Adapter.Chon_Dia_Diem_Adapter;
import com.example.syluanit.myapplication.Adapter.PlaceAutocompleteAdapter;
import com.example.syluanit.myapplication.Model.DiaDiem;
import com.example.syluanit.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNewRoadActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        map = googleMap;
        if (LocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    COURSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    //Add click-point maker latlag to listpoint and set maker whenever click on map
                    listPoints.add(latLng);
                    MarkerOptions options = new MarkerOptions()
                            .position(latLng);

                    Marker marker = map.addMarker(options);
                    //store maker in hashmap with key
                    markerHashMap.put("maker_new_road" + listPoints.size(),marker);

                    if (listPoints.size() > 1)
                    {
                        //set view the button back
                        btnBack.setVisibility(View.VISIBLE);
                        btnBack.bringToFront();
                        //add the direction
                        PolylineOptions polylineOptions = new PolylineOptions().
                                geodesic(true).
                                color(Color.BLUE).
                                width(10);
                        polylineOptions.add(listPoints.get(listPoints.size()-2));
                        polylineOptions.add(listPoints.get(listPoints.size()-1));
                        //Store polyline in hashmapp with key
                        Polyline polyline = map.addPolyline(polylineOptions);
                        polylineHashMap.put("polyline_new_road" + listPoints.size(),polyline);
                    }
                }
            });

            //Check the view of Back button
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //remove the last added maker
                        Marker marker = markerHashMap.get("maker_new_road" + listPoints.size());
                        marker.remove();
                        //remove the last added polyline
                        Polyline polyline = polylineHashMap.get("polyline_new_road"+ listPoints.size());
                        polyline.remove();

                        listPoints.remove(listPoints.size() - 1);
                        //check the conditional view of button
                        if (listPoints.size() == 1) {
                            btnBack.setVisibility(View.GONE);}
                    }
                });

        }
    }

    private static final String TAG = "AddNewRoad";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private List<Polyline> polylinePaths = new ArrayList<>();

    // vars
    private Boolean LocationPermissionsGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    ArrayList<LatLng> listPoints;
    HashMap<String, Marker> markerHashMap;
    HashMap<String, Polyline> polylineHashMap;
    //widgets
    Button btnBack, btnAdd;
    RelativeLayout relativeLayout;
    Spinner spinnerFrom, spinnerTo;
    Chon_Dia_Diem_Adapter chon_dia_diem_adapter_new_road;
    ArrayList<DiaDiem> diaDiemArrayList_new_road;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_road);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);

        diaDiemArrayList_new_road = new ArrayList<>();
        diaDiemArrayList_new_road.add(new DiaDiem("Binh Định"));
        diaDiemArrayList_new_road.add(new DiaDiem("HCM"));
        diaDiemArrayList_new_road.add(new DiaDiem("Nha Trang"));
        diaDiemArrayList_new_road.add(new DiaDiem("Phan Thiết"));
        diaDiemArrayList_new_road.add(new DiaDiem("Đa Nẵng"));

        chon_dia_diem_adapter_new_road = new Chon_Dia_Diem_Adapter(this, R.layout.dia_diem_item ,diaDiemArrayList_new_road);
        spinnerFrom.setAdapter(chon_dia_diem_adapter_new_road);
        spinnerTo.setAdapter(chon_dia_diem_adapter_new_road);

        listPoints = new ArrayList<>();
        markerHashMap = new HashMap<>();
        polylineHashMap = new HashMap<>();

        getLocationPermission();
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myAddNewRoadMap);

        mapFragment.getMapAsync(this);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (LocationPermissionsGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentlocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude())
                                    , DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(AddNewRoadActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException:" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.e(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!(title == "My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            map.addMarker(options);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            LocationPermissionsGranted = false;
                            return;
                        }
                    }
                    LocationPermissionsGranted = true;
                    // initialize our map
                }
            }
        }
    }

}

