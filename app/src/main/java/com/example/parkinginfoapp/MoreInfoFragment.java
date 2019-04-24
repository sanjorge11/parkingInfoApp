package com.example.parkinginfoapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoreInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoreInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MoreInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreInfoFragment newInstance(String param1, String param2) {
        MoreInfoFragment fragment = new MoreInfoFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1(v);
            }
        });

        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2(v);
            }
        });

        Button button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3(v);
            }
        });

        Button button4 = view.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button4(v);
            }
        });

        Button button5 = view.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button5(v);
            }
        });

        Button button6 = view.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button6(v);
            }
        });

        Button button7 = view.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button7(v);
            }
        });

        Button button8 = view.findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button8(v);
            }
        });

        Button button9 = view.findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button9(v);
            }
        });

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

    public void button1(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://move.unc.edu/about/online-services/"));
        startActivity(browserIntent);
    }

    public void button2(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://move.unc.edu/transit/"));
        startActivity(browserIntent);
    }

    public void button3(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://move.unc.edu/parking/"));
        startActivity(browserIntent);
    }

    public void button4(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://move.unc.edu/cap/"));
        startActivity(browserIntent);
    }

    public void button5(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://move.unc.edu/about/pricing/"));
        startActivity(browserIntent);
    }

    public void button6(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://move.unc.edu/events/"));
        startActivity(browserIntent);
    }

    public void button7(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://translocrider.com/"));
        startActivity(browserIntent);
    }

    public void button8(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://parkmobile.io/"));
        startActivity(browserIntent);
    }

    public void button9(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://move.unc.edu"));
        startActivity(browserIntent);
    }

}
