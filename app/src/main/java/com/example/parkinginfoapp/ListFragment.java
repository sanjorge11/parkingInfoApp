package com.example.parkinginfoapp;

import android.Manifest;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.parkinginfoapp.dummy.DummyContent;
import com.example.parkinginfoapp.dummy.DummyContent.DummyItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.support.constraint.Constraints.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment implements LocationListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    LocationManager locationManager;
    private final int REQ_PERMISSION = 101;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState == null) {
            System.out.println("new isntance of list");
        } else {
            System.out.println(savedInstanceState.getInt("val"));
        }

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        getLocation();
        System.out.println();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        /*View viewItem = inflater.inflate(R.layout.fragment_item, container, false);


        Button carIconButton = (Button) viewItem.findViewById(R.id.carIcon);
        carIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button pressed");
            }
        }); */

        System.out.println();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            //read lots
            new FirebaseDatabaseHelper().readLots(new FirebaseDatabaseHelper.DataStatus_Lots() {
                @Override
                public void DataIsLoaded(List<Lot> lots, List<String> keys) {

                    List<Lot> sortedList = displaySortedList(lots);     //optimize this

                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(sortedList, mListener));
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


        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }





        public List<Lot> displaySortedList(List<Lot> lots) {
        //LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        List<Lot> sortedList;

        if(checkPermission()) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Lot[] lotArr = lots.toArray(new Lot[lots.size()]);
            for(int i=0; i<lots.size(); i++) {
                for(int j=i+1; j<lots.size(); j++) {
                    Location dest1 = new Location(LocationManager.GPS_PROVIDER);
                    dest1.setLatitude(lotArr[i].getLatitude());
                    dest1.setLongitude(lotArr[i].getLongitude());

                    Location dest2 = new Location(LocationManager.GPS_PROVIDER);
                    dest2.setLatitude(lotArr[j].getLatitude());
                    dest2.setLongitude(lotArr[j].getLongitude());

                    if(location.distanceTo(dest1) > location.distanceTo(dest2)) {
                        Lot temp = lotArr[i];
                        lotArr[i] = lotArr[j];
                        lotArr[j] = temp;
                    } else { } //sorted
                }
            }

            sortedList = Arrays.asList(lotArr);

            //LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            //LatLng dest = new LatLng(lots.get(0).getLatitude(), lots.get(0).getLongitude());

            //String url = getRequestURL(currentLocation, dest);

        } else{
            askPermission();
            return displaySortedList(lots);
        }
        return sortedList;
    }

    public void getGoogleMapsDirections(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+ "-33.8567844,151.213108");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    /*public String getRequestURL(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;

        //Value of dest
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        //Mode for find direction
        String mode = "mode=driving";

        //Build full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;

        //output format
        String output = "json";

        //create url to request
        String url =
    } */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Lot item);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outstate) {
//        super.onSaveInstanceState(outstate);
//
//        outstate.putInt("val", 12);
//
//        System.out.println("save");
//
//    }

    /////

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


    //Locations
    void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this); //change parameters?
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
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
}
