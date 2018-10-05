package com.example.syluanit.myapplication.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.syluanit.myapplication.Activity.Home;
import com.example.syluanit.myapplication.Adapter.Chon_Dia_Diem_Adapter;
import com.example.syluanit.myapplication.Interface.DirectionFinderListener;
import com.example.syluanit.myapplication.Model.DiaDiem;
import com.example.syluanit.myapplication.Model.Route;
import com.example.syluanit.myapplication.R;
import com.example.syluanit.myapplication.Service.DirectionFinder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Fragment_Duong_Di_Moi extends Fragment implements OnMapReadyCallback, DirectionFinderListener,
        GoogleMap.OnPolylineClickListener {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        final LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        final boolean[] gps_enabled = {false};
        boolean network_enabled = false;

        try {
            gps_enabled[0] = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}


        if(!gps_enabled[0] && !network_enabled) {
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
                        FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),
                        COURSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);

//            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//                    //Add click-point maker latlag to listpoint and set maker whenever click on map
//                    listPoints.add(latLng);
//                    MarkerOptions options = new MarkerOptions()
//                            .position(latLng);
//
//                    Marker marker = map.addMarker(options);
//                    //store maker in hashmap with key
//                    markerHashMap.put("maker_new_road" + listPoints.size(),marker);
//
//                    if (listPoints.size() > 1)
//                    {
//                        //set view the button back
//                        btnBack.setVisibility(View.VISIBLE);
//                        btnBack.bringToFront();
//                        //add the direction
//                        PolylineOptions polylineOptions = new PolylineOptions().
//                                geodesic(true).
//                                color(Color.BLUE).
//                                width(10);
//                        polylineOptions.add(listPoints.get(listPoints.size()-2));
//                        polylineOptions.add(listPoints.get(listPoints.size()-1));
//                        //Store polyline in hashmapp with key
//                        Polyline polyline = map.addPolyline(polylineOptions);
//                        polylineHashMap.put("polyline_new_road" + listPoints.size(),polyline);
//                    }
//                }
//            });
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRequest();
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
                        Polyline polyline = polylineHashMap.get("polyline_new_road" + listPoints.size());
                        polyline.remove();

                        listPoints.remove(listPoints.size() - 1);
                        //check the conditional view of button
                        if (listPoints.size() == 1) {
                            btnBack.setVisibility(View.GONE);
                        }
                    }
                });

                map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        map.clear();
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

//                        List <Address> addresses =  geocoder.getFromLocation(marker.getPosition().latitude,
//                                marker.getPosition().longitude, 1);
//
//                        String changePostion = addresses.get(0).getAddressLine(0);
//                        Log.d(TAG, "onMarkerDragEnd: "+ changePostion);

                        String changePostion = getCompleteAddressString(marker.getPosition());

//                        List <Address> addresses1 =  geocoder.getFromLocation(originMarkers.get(0).getPosition().latitude,
//                                originMarkers.get(0).getPosition().longitude, 1);
//
//                        String startAddress = addresses1.get(0).getAddressLine(0);
//
//                        Log.d(TAG, "onMarkerDragEnd: " + startAddress);
//
//                        List <Address> addresses2 =  geocoder.getFromLocation(destinationMarkers.get(0).getPosition().latitude,
//                                destinationMarkers.get(0).getPosition().longitude, 1);
//
//                        String endAddress = addresses2.get(0).getAddressLine(0);
//
//                        Log.d(TAG, "onMarkerDragEnd: " + endAddress);

                        String endAddress = getCompleteAddressString(destinationMarkers.get(0).getPosition());
                        String startAddress = getCompleteAddressString(originMarkers.get(0).getPosition());
                        sign = 1;
                        try {
                            new DirectionFinder(Fragment_Duong_Di_Moi.this, "quan 1", changePostion).execute();
                            new DirectionFinder(Fragment_Duong_Di_Moi.this, changePostion, "quan 3").execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

//                    LatLng changePosition = marker.getPosition();
//                    LatLng startAddress = originMarkers.get(0).getPosition();
//                    LatLng endAddress = destinationMarkers.get(0).getPosition();


                        moveCamera(marker.getPosition(), DEFAULT_ZOOM, "Changed");
                    }
                });

            }
        }
    }

    private String getCompleteAddressString(LatLng latLng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w(TAG, strReturnedAddress.toString());
            } else {
                Log.w(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Canont get Address!");
        }
        return strAdd;
    }

    private static final int ERROR_DIALOG_REQUEST = 9001;
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
    Button btnBack, btnAdd, btnShow;
    RelativeLayout relativeLayout;
    Spinner spinnerFrom, spinnerTo;
    Chon_Dia_Diem_Adapter chon_dia_diem_adapter_new_road;
    ArrayList<DiaDiem> diaDiemArrayList_new_road;
    View view;
    private ProgressDialog progressDialog1;
    private int sign = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_duong_di_moi, container, false);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnShow = (Button) view.findViewById(R.id.btnShow);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        spinnerFrom = (Spinner) view.findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) view.findViewById(R.id.spinnerTo);

        diaDiemArrayList_new_road = new ArrayList<>();
        diaDiemArrayList_new_road.add(new DiaDiem("Bình Định"));
        diaDiemArrayList_new_road.add(new DiaDiem("HCM"));
        diaDiemArrayList_new_road.add(new DiaDiem("Nha Trang"));
        diaDiemArrayList_new_road.add(new DiaDiem("Phan Thiết"));
        diaDiemArrayList_new_road.add(new DiaDiem("Đà Nẵng"));

        chon_dia_diem_adapter_new_road = new Chon_Dia_Diem_Adapter(getContext(), R.layout.dia_diem_item, diaDiemArrayList_new_road);
        spinnerFrom.setAdapter(chon_dia_diem_adapter_new_road);
        spinnerTo.setAdapter(chon_dia_diem_adapter_new_road);

        listPoints = new ArrayList<>();
        markerHashMap = new HashMap<>();
        polylineHashMap = new HashMap<>();

        getLocationPermission();

        sign = 0;
        return view;
    }

    private void initMap() {
//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myAddNewRoadMap);
//        mapFragment.getMapAsync(this);
        MapView mapView = (MapView) view.findViewById(R.id.myMap_addNewRoad);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

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
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
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

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void sendRequest(){
        String start = "quan 1";
        String end = "quan 3";
        try {
            new DirectionFinder(Fragment_Duong_Di_Moi.this, start, end).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private ProgressDialog progressDialog;

    @Override
    public void onDirectionFinderStart() {
        if ( progressDialog != null && progressDialog.isShowing() ) {}
        else{
            progressDialog = ProgressDialog.show(getContext(), "Làm ơn đợi.",
                    "Đang tìm đường..!", true);
        }

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        Log.d(TAG, "onDirectionFinderSuccess: ");
        progressDialog.dismiss();

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            if (sign != 1){
            moveCamera(route.startLocation, 16,"");}
//            ((TextView) view.findViewById(R.id.tvDuration)).setText(route.duration.text
//                    + "(" +route.distance.text + ")");
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(map.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.defaultMarker(ContextCompat.getColor(getContext(), R.color.colorPrimary)))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(map.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .position(route.endLocation)));
//                    .icon(BitmapDescriptorFactory.defaultMarker(ContextCompat.getColor(getContext(), R.color.colorPrimary)))));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(ContextCompat.getColor(getContext(),R.color.colorPrimary)).
                    width(10);

            for (int i = 0; i < route.points.size(); i++){
                polylineOptions.add(route.points.get(i));
                {
                    map.addMarker(new MarkerOptions().
                            position(route.points.get(i)))
//                            .icon(BitmapDescriptorFactory.defaultMarker(ContextCompat.getColor(getContext(), R.color.colorPrimary))))
                            .setDraggable(true)
                    ;
                }
              }

//            Log.d(TAG, "onDirectionFinderSuccess: " + polylineOptions.toString());
            polylinePaths.add(map.addPolyline(polylineOptions));
//            Polyline polyline = map.addPolyline(polylineOptions);
//            polyline.setClickable(true);
//            map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
//                @Override
//                public void onPolylineClick(Polyline polyline) {
//                    Toast.makeText(getActivity(), "yeah yeah", Toast.LENGTH_SHORT).show();
//                }
//            });



        }
    }


    @Override
    public void onPolylineClick(Polyline polyline) {
        Toast.makeText(getActivity(), "yeah yeah", Toast.LENGTH_SHORT).show();

    }

}
