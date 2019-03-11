package com.example.parkinginfoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkinginfoapp.dummy.DummyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        ListFragment.OnListFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        MoreInfoFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            View navHost_View = findViewById(R.id.nav_host);

            switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Navigation.findNavController(navHost_View).navigate(R.id.main_dest);
                        return true;
                    case R.id.navigation_listView:
                        Navigation.findNavController(navHost_View).navigate(R.id.list_dest);
                        return true;
                    case R.id.navigation_profile:
                        Navigation.findNavController(navHost_View).navigate(R.id.profile_dest);
                        return true;
                    case R.id.navigation_moreInfo:
                        Navigation.findNavController(navHost_View).navigate(R.id.moreinfo_dest);
                        return true;
            }


               // getSupportFragmentManager().beginTransaction().replace(R.id.)

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println();

        if(currentUser == null) {       //if null, then not signed in
            Intent myIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            MainActivity.this.startActivity(myIntent);
        }

        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user) {
      Toast.makeText(getApplicationContext(), "Update UI" ,
                    Toast.LENGTH_SHORT).show();
    }

    public void signOutClicked(View view) {
        FirebaseAuth.getInstance().signOut();

        FirebaseUser user = mAuth.getCurrentUser();

        backToWelcomePage(view);

        updateUI(user);
    }

    public void backToWelcomePage(View view){
        Intent myIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        MainActivity.this.startActivity(myIntent);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        //nothing right now
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.i("Navigation", "Selected " + item);
    }





}
