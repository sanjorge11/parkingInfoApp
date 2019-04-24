package com.example.parkinginfoapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.icu.text.DateIntervalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkinginfoapp.dummy.DummyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        ListFragment.OnListFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        MoreInfoFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


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


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications","MyNotifications ", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        /*
        Button signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutClicked(v);
            }
        }); */
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        System.out.println();

        if(currentUser == null) {       //if null, then not signed in
            Intent myIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            MainActivity.this.startActivity(myIntent);
        } else {
            updateUI(currentUser);
        }
    }

    public void updateUI(FirebaseUser user) {
      Toast.makeText(getApplicationContext(), "Logged in as: " + user.getEmail(),
                    Toast.LENGTH_SHORT).show();

      //String uid = user.getUid();
      System.out.println();
    }

    public String getUID() {
        return currentUser.getUid();
    }

    public void signOutClicked(View view) {
        FirebaseAuth.getInstance().signOut();

        FirebaseUser user = mAuth.getCurrentUser();

        backToWelcomePage(view);

        //updateUI(user);
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
    public void onListFragmentInteraction(Lot item) {
        //saddr=" + item.getLatitude() + "," + item.getLongitude() + "&
        String url = "http://maps.google.com/maps?daddr=" + item.getLatitude() + "," + item.getLongitude();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(intent);
        //System.out.println("Selected item on List ---> " + item.getLotName() + " Coordinates: (" + item.getLongitude() + "," + item.getLatitude() + ")");
        //Log.i("Navigation", "Selected " + item);
    }








}
