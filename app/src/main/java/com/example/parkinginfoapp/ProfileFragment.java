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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Ref;
import java.util.ArrayList;
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

       // DatabaseReference dbRef = database.getReferenceFromUrl("https://unc-parking-app.firebaseio.com/users");
        new FirebaseDatabaseHelper().readUsers(new FirebaseDatabaseHelper.DataStatus_Users() {
            @Override
            public void DataIsLoaded(List<User> users, List<String> keys) {

                EditText nameEditText = (EditText) getView().findViewById(R.id.name);
                String current_name = users.get(0).firstName + " " + users.get(0).lastName;
                nameEditText.setText(current_name);
                nameEditText.setEnabled(false);

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
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

//        new FirebaseDatabaseHelper().readPermits(new FirebaseDatabaseHelper.DataStatus_Permits() {
//            @Override
//            public void DataIsLoaded(List<Permit> permits, List<String> keys) {
//                permitsResponse = permits;
//            }
//
//            @Override
//            public void DataIsInserted() {
//
//            }
//
//            @Override
//            public void DataIsUpdated() {
//
//            }
//
//            @Override
//            public void DataIsDeleted() {
//
//            }
//        });

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        Button toggleButton = (Button) view.findViewById(R.id.pushNotificationsToggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEnable(v);
            }
        });  */

        Spinner spinnerUser = view.findViewById(R.id.userTypeSpinner);
        ArrayAdapter<CharSequence> adapterUser = ArrayAdapter.createFromResource(getContext(), R.array.profileOptions, android.R.layout.simple_spinner_item);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(adapterUser);
        spinnerUser.setOnItemSelectedListener(this);


        Spinner spinnerPushNotifications = view.findViewById(R.id.pushNotificationsSpinner);
        ArrayAdapter<CharSequence> adapterPushNotifications = ArrayAdapter.createFromResource(getContext(), R.array.pushNotificationsOptions ,android.R.layout.simple_spinner_item);
        adapterPushNotifications.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPushNotifications.setAdapter(adapterPushNotifications);
        spinnerPushNotifications.setOnItemSelectedListener(this);


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
                spinnerPermits.setAdapter(adapterPermits);
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
        System.out.println("Spinner selected item: " + text);
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
