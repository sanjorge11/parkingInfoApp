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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.example.parkinginfoapp.dummy.DummyContent;
import com.example.parkinginfoapp.dummy.DummyContent.DummyItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.support.constraint.Constraints.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

                                                        //, AdapterView.OnItemSelectedListener
public class ListFragment extends Fragment implements LocationListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    User currentUserRef;
    String currentUserPermit;
    String userActualPermit;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private MyItemRecyclerViewAdapter adapterLots;

    /*
    Spinner spinnerPermits;
    List<String> permitStrings = new ArrayList<>();
*/


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
            System.out.println("new instance of list");
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

        final View listView = view.findViewById(R.id.list);

        System.out.println();




        // Set the adapter
        if (listView instanceof RecyclerView) {
            Context context = listView.getContext();
            final RecyclerView recyclerView = (RecyclerView) listView;
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

                    adapterLots = new MyItemRecyclerViewAdapter(sortedList, mListener);

                    recyclerView.setAdapter(adapterLots);
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
        });

        spinnerPermits = view.findViewById(R.id.listPermitSpinner);
        new FirebaseDatabaseHelper().readPermits(new FirebaseDatabaseHelper.DataStatus_Permits() {
            @Override
            public void DataIsLoaded(List<Permit> permits, List<String> keys) {
               // permitsResponse = permits;

                //List<String> permitStrings = new ArrayList<>();
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

                filterRecyclerView(adapterLots);
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
        */

        /////////
        /*
        Button filterButton = (Button) view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterRecyclerView(adapterLots);
            }
        });

        Button clearButton = (Button) view.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLots(adapterLots);
            }
        }); */


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

    /*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        if(parent.getId() == R.id.listPermitSpinner) {
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
/*
    public void filterRecyclerView(MyItemRecyclerViewAdapter adapter) {
        adapter.getFilter().filter(currentUserPermit);
    }

    public void getMyLots(MyItemRecyclerViewAdapter adapter) {

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

        adapter.getFilter().filter(userActualPermit);
    }
    */
}
