package com.example.parkinginfoapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Spinner SpinnerProfile;
    List<Permit> permitsResponse = new ArrayList<>();


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    List<String> profileOptions;
    User currentUserRef;
    String currentUserType;
    String currentUserPermit;

    private boolean pushNotificationsEnabled;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        String uid = currentUser.getUid();
        //String uid = "xRDqXuWgHrQI3YCf2icXvDkbsgJ2";

       // DatabaseReference dbRef = database.getReferenceFromUrl("https://unc-parking-app.firebaseio.com/users");





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button saveChangesButton = (Button) view.findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(v);
            }
        });


        final Spinner spinnerUser = view.findViewById(R.id.userTypeSpinner);
        ArrayAdapter<CharSequence> adapterUser = ArrayAdapter.createFromResource(getContext(), R.array.profileOptions, android.R.layout.simple_spinner_item);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(adapterUser);

        new FirebaseDatabaseHelper().readCurrentUser(currentUser.getUid(), new FirebaseDatabaseHelper.DataStatus_CurrentUser() {
            @Override
            public void DataIsLoaded(User user, String key) {

                EditText nameEditText = (EditText) view.findViewById(R.id.name);
                String current_name = user.firstName + " " + user.lastName;
                nameEditText.setText(current_name);
                nameEditText.setEnabled(false);

                String userType = user.getType();
                currentUserType = user.getType();
                profileOptions = Arrays.asList(getResources().getStringArray(R.array.profileOptions));

                currentUserRef = user;

                int userTypeSelected = 0;
                for(int i=0; i<profileOptions.size(); i++) {
                    if(userType.equals(profileOptions.get(i)))  userTypeSelected = i;
                }

                spinnerUser.setSelection(userTypeSelected);

                System.out.println();

                /*
                EditText userTypeEditText = (EditText) getView().findViewById(R.id.userType);
                String current_userType = users.get(0).type;
                userTypeEditText.setText(current_userType); */

                /*
                EditText permitEditText = (EditText) getView().findViewById(R.id.permits);
                String current_permits = users.get(0).permits.get(0); //get first, this is for testing
                permitEditText.setText(current_permits); */

                /*
                pushNotificationsEnabled = users.get(0).push_notifications;
                setTextPushNotifications(getView(), pushNotificationsEnabled); */
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

        spinnerUser.setOnItemSelectedListener(this);



/*
        Spinner spinnerPushNotifications = view.findViewById(R.id.pushNotificationsSpinner);
        ArrayAdapter<CharSequence> adapterPushNotifications = ArrayAdapter.createFromResource(getContext(), R.array.pushNotificationsOptions ,android.R.layout.simple_spinner_item);
        adapterPushNotifications.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPushNotifications.setAdapter(adapterPushNotifications);
        spinnerPushNotifications.setOnItemSelectedListener(this);
*/

        final Spinner spinnerPermits = view.findViewById(R.id.permitsSpinner);
        new FirebaseDatabaseHelper().readPermits(new FirebaseDatabaseHelper.DataStatus_Permits() {
            @Override
            public void DataIsLoaded(List<Permit> permits, List<String> keys) {
                permitsResponse = permits;

                List<String> permitStrings = new ArrayList<>();
                for(int i=0; i<permits.size(); i++) {
                    permitStrings.add(permits.get(i).permit_type);
                }

                Collections.sort(permitStrings);

                ArrayAdapter<String> adapterPermits = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, permitStrings);
                adapterPermits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                int permitSelected = 0;
                List<String> currentPermit = currentUserRef.getPermits();
                currentUserPermit = currentPermit.get(0) != null && currentPermit.size() > 0 ? currentPermit.get(0) : "";
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
        String text = parent.getItemAtPosition(pos).toString();

        if(parent.getId() == R.id.userTypeSpinner) {
            currentUserType = text;

            System.out.println("User Type Spinner selected item: " + text);
        } else if (parent.getId() == R.id.permitsSpinner) {
            currentUserPermit = text;

            System.out.println("Permit Spinner selected item: " + text);
        } else { }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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


    public void saveChanges(View view) {


        currentUserRef.setType(currentUserType);
        ArrayList<String> permits = new ArrayList<>();
        permits.add(currentUserPermit);
        currentUserRef.setPermits(permits);
        System.out.println();


        new FirebaseDatabaseHelper().insertCurrentUser(currentUser.getUid(), currentUserRef, new FirebaseDatabaseHelper.DataStatus_CurrentUser() {
            @Override
            public void DataIsLoaded(User user, String key) {

            }

            @Override
            public void DataIsInserted(User user) {

                //System.out.println(user);
                //System.out.println("success");
            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        //System.out.println(currentUserType);
        //System.out.println(currentUserPermit);
        //System.out.println();
    }

    /*
    public void toggleEnable(View view) {
        pushNotificationsEnabled = !pushNotificationsEnabled;
        setTextPushNotifications(view, pushNotificationsEnabled);
    }

    public void setTextPushNotifications(View view, boolean pushNotificationsEnabled) {
        Button toggleButton = (Button) view.findViewById(R.id.pushNotificationsToggle);

        if(pushNotificationsEnabled) {
            toggleButton.setText("Enabled");
        } else {
            toggleButton.setText("Disabled");
        }

    } */

}
