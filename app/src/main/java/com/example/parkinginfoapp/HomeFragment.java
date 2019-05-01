package com.example.parkinginfoapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.MapFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.location.LocationManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
                                                                //, AdapterView.OnItemSelectedListener
public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final int REQ_PERMISSION = 101;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mGoogleMap;
    MapView mMapView;
    private List<Lot> markers = new ArrayList<>();
    private List<Lot> lotsResponse = new ArrayList<>();
    LocationManager locationManager;
    /*
    Spinner spinnerPermits;
    List<String> permitStrings = new ArrayList<>();
    */
    User currentUserRef;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    /*
    String currentUserPermit;
    String userActualPermit;
    private MyItemRecyclerViewAdapter adapterLots;
*/


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLocation();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.map);


        if(mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*
        spinnerPermits = view.findViewById(R.id.mapPermitSpinner);
        */

        /*
        new FirebaseDatabaseHelper().readCurrentUser(currentUser.getUid(), new FirebaseDatabaseHelper.DataStatus_CurrentUser() {
            @Override
            public void DataIsLoaded(User user, String key) {

                currentUserRef = user;

                System.out.println();
            }

            @Override
            public void DataIsInserted(User user) {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        }); */

        /*
        new FirebaseDatabaseHelper().readPermits(new FirebaseDatabaseHelper.DataStatus_Permits() {
            @Override
            public void DataIsLoaded(List<Permit> permits, List<String> keys) {


                for(int i=0; i<permits.size(); i++) {
                    permitStrings.add(permits.get(i).permit_type);
                }

                Collections.sort(permitStrings);
                permitStrings.add(0, "All");

                ArrayAdapter<String> adapterPermits = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, permitStrings);
                adapterPermits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                int permitSelected = 0;
                List<String> currentPermit = currentUserRef.getPermits();
                currentUserPermit = currentPermit.get(0) != null && currentPermit.size() > 0 ? currentPermit.get(0) : "";
                userActualPermit = currentPermit.get(0) != null && currentPermit.size() > 0 ? currentPermit.get(0) : "";

                if(currentPermit != null && currentPermit.size() > 0) {     //should be size 1 always
                    for(int i=0; i<permitStrings.size(); i++) {
                        if(permitStrings.get(i).equals(currentPermit.get(0))) {
                            permitSelected = i;
                            break;
                        }
                    }
                }

                spinnerPermits.setAdapter(adapterPermits);
                spinnerPermits.setSelection(permitSelected);

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        spinnerPermits.setOnItemSelectedListener(this);


        Button filterButton = (Button) view.findViewById(R.id.filterButtonMap);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMap(mGoogleMap);
            }
        });

        Button myLotButton = (Button) view.findViewById(R.id.clearButtonMap);
        myLotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLots(mGoogleMap);
            }
        });
        */

        // Inflate the layout for this fragment
        return view; //inflater.inflate(R.layout.fragment_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        if(parent.getId() == R.id.mapPermitSpinner) {
            currentUserPermit = text;

            System.out.println("User Type Spinner selected item: " + text);
        }  else { }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        if(checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if(location == null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); //change parameters?
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null) {
                moveCameraToCurrentLocation(location);


                new FirebaseDatabaseHelper().readLots(new FirebaseDatabaseHelper.DataStatus_Lots() {
                    @Override
                    public void DataIsLoaded(List<Lot> lots, List<String> keys) {

                        for(int i=0; i<lots.size(); i++) {
                            Double latitude = lots.get(i).getLatitude();
                            Double longitude = lots.get(i).getLongitude();
                            String lot_name = lots.get(i).getLotName();
                            String lot_number = lots.get(i).getLotNumber();
                            String permit_type = lots.get(i).getPermitType();
                            String lot_time = lots.get(i).getTime();

                            LatLng marker = new LatLng(latitude, longitude);
                            mGoogleMap.addMarker(new MarkerOptions().position(marker).title(lot_name).snippet("Available with " + permit_type + " pass"));

                            markers.add(lots.get(i));
                        }
                        //lotsResponse = lots;
                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });



                /* Street-view 3D zoom
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); */

            }


        }
        else{
            askPermission();
        }

    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    public void moveCameraToCurrentLocation(Location location) {
        //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));

        //move camera to current location in one frame rather than animation
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
    }

    //Permissions
    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQ_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    if(checkPermission())
                        mGoogleMap.setMyLocationEnabled(true);

                } else {
                    // Permission denied
                }
                break;
            }
        }
    }

    /////
    //Locations

    void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        moveCameraToCurrentLocation(location);
        System.out.println("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    /////
    /*
    public void filterMap(GoogleMap gMap) {
        gMap.clear();

        //currentUserPermit
        for(int i=0; i<markers.size(); i++) {
            Double latitude = markers.get(i).getLatitude();
            Double longitude = markers.get(i).getLongitude();
            String lot_name = markers.get(i).getLotName();
            String lot_number = markers.get(i).getLotNumber();
            String permit_type = markers.get(i).getPermitType();
            String lot_time = markers.get(i).getTime();

            if(permit_type.equals(currentUserPermit)) {
                LatLng marker = new LatLng(latitude, longitude);
                mGoogleMap.addMarker(new MarkerOptions().position(marker).title(lot_name).snippet("Available with " + permit_type + " pass"));
            }
        }
    }

    public void getMyLots(GoogleMap gMap) {

        int permitSelected = 0;
        List<String> currentPermit = currentUserRef.getPermits();
        currentUserPermit = currentPermit.get(0) != null && currentPermit.size() > 0 ? currentPermit.get(0) : "";
        userActualPermit = currentPermit.get(0) != null && currentPermit.size() > 0 ? currentPermit.get(0) : "";

        if(currentPermit != null && currentPermit.size() > 0) {     //should be size 1 always
            for(int i=0; i<permitStrings.size(); i++) {
                if(permitStrings.get(i).equals(currentPermit.get(0))) {
                    permitSelected = i;
                    break;
                }
            }
        }

        spinnerPermits.setSelection(permitSelected);

        /////////////////
        gMap.clear();

        //currentUserPermit
        for(int i=0; i<markers.size(); i++) {
            Double latitude = markers.get(i).getLatitude();
            Double longitude = markers.get(i).getLongitude();
            String lot_name = markers.get(i).getLotName();
            String lot_number = markers.get(i).getLotNumber();
            String permit_type = markers.get(i).getPermitType();
            String lot_time = markers.get(i).getTime();

            if(permit_type.equals(userActualPermit)) {
                LatLng marker = new LatLng(latitude, longitude);
                mGoogleMap.addMarker(new MarkerOptions().position(marker).title(lot_name).snippet("Available with " + permit_type + " pass"));
            }
        }
    } */

}
