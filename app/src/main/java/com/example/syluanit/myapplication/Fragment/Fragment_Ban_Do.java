package com.example.syluanit.myapplication.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syluanit.myapplication.Activity.Home;
import com.example.syluanit.myapplication.Activity.MainActivity;
import com.example.syluanit.myapplication.Adapter.PlaceAutocompleteAdapter;
import com.example.syluanit.myapplication.Interface.DirectionFinderListener;
import com.example.syluanit.myapplication.Model.Route;
import com.example.syluanit.myapplication.R;
import com.example.syluanit.myapplication.Service.DirectionFinder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_Ban_Do extends Fragment implements OnMapReadyCallback,
        DirectionFinderListener {

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();
        MapsInitializer.initialize(getContext());
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
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);

                init();
            }
        }
    }

    private static final String TAG = "MAP";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168),
            new LatLng(71, 136));

    // vars
    private Boolean LocationPermissionsGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
     @Nullable private GoogleMap map;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    //    private GoogleApiClient mGoogleApiClient;
    private GeoDataClient geoDataClient;
    private Place mPlace;
    //widgets
    View view;
    private AutoCompleteTextView et_from, et_to;
    private MapView mapView;
    private ConstraintLayout myLayout;
    private TextView tvDuration;
    private LinearLayout distanceLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ban_do, container, false);
        et_from = (AutoCompleteTextView) view.findViewById(R.id.et_diem_di_map);
        et_to = (AutoCompleteTextView) view.findViewById(R.id.et_diem_den_map);
        tvDuration = (TextView) view.findViewById(R.id.tvDuration);
        myLayout = (ConstraintLayout) view.findViewById(R.id.myLayout);
        distanceLayout = (LinearLayout) view.findViewById(R.id.DistanceLayout);
        myLayout.bringToFront();

        if ( isServicesOK()) {
            getLocationPermission();
            et_from.setText("");
            et_to.setText("");
            tvDuration.setText("");
        }

        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }


    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if (available == ConnectionResult.SUCCESS) {
            // everything is fine and user can make map requests
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "You can't make map requets", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initMap() {
        mapView = (MapView) view.findViewById(R.id.myMap);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        geoDataClient = Places.getGeoDataClient(getContext(), null);

        et_from.setOnItemClickListener(mAutocompleteClickListener);
        et_to.setOnItemClickListener(mAutocompleteClickListener);

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), geoDataClient,
                LAT_LNG_BOUNDS, null);

        et_from.setAdapter(placeAutocompleteAdapter);
        et_to.setAdapter(placeAutocompleteAdapter);

        et_from.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    // execute our searching method

                    geoLocate();
                }
                return false;
            }
        });

        et_to.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    // execute our searching method

                    geoLocate();
                    sendRequest();
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = et_from.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();

        if (Geocoder.isPresent()) {

            try {
                list = geocoder.getFromLocationName(searchString, 1);
            } catch (IOException e) {
                Log.e(TAG, "geoLocate: " + e.getMessage());
            }
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
//            Toast.makeText(getActivity(), address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM
                    , address.getAddressLine(0));
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
                            if (currentlocation != null) {
                                moveCamera(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude())
                                        , DEFAULT_ZOOM, "My Location");
                            }
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
//                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_location));
            map.addMarker(options);
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

    private void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null){
            map.clear();
        }
    }

    /*
---------------------------- google places API complete suggestions-------------------
 */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();

            Task<PlaceBufferResponse> placeResult = geoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };


    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            PlaceBufferResponse places = task.getResult();

            // Get the Place object from the buffer.
            final Place place = places.get(0);
            try {
                Log.d(TAG, "onResult: " + place.toString());
                mPlace = place;
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName().toString());

            places.release();
        }
    };

    /*
------------------------------- Google Map API Directions --------------------------------
 */

    private void sendRequest() {
        String origin = et_from.getText().toString();
        String destination = et_to.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(getActivity(), "Bạn chưa nhập điểm đi!", Toast.LENGTH_SHORT).show();
            return;
        } else if (destination.isEmpty()) {
            Toast.makeText(getActivity(), "Bạn chưa nhập điểm đến!", Toast.LENGTH_SHORT).show();
            return;
        }
        else map.clear();

        try {
            new DirectionFinder(Fragment_Ban_Do.this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
            progressDialog = ProgressDialog.show(getContext(), "Làm ơn đợi.",
                    "Đang tìm đường..!", true);

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

    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(1);
    //
// Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        Log.d(TAG, "onDirectionFinderSuccess: ");

        progressDialog.dismiss();
        distanceLayout.setVisibility(View.VISIBLE);

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) view.findViewById(R.id.tvDuration)).setText(route.duration.text
                    + "(" +route.distance.text + ")");
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(map.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(map.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(ContextCompat.getColor(getContext(),R.color.colorPrimary)).
                    width(10).
                    pattern(PATTERN_POLYLINE_DOTTED).
//                    endCap(new RoundCap()).
                    jointType(JointType.ROUND);
//                    .startCap(new CustomCap(
//                            BitmapDescriptorFactory.fromResource(R.mipmap.location), 10));

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

//            Log.d(TAG, "onDirectionFinderSuccess: " + polylineOptions.toString());
            polylinePaths.add(map.addPolyline(polylineOptions));

        }
    }
}