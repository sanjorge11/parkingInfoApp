package com.example.parkinginfoapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.parkinginfoapp.dummy.DummyContent;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        ListFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;

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
                    return true;
                case R.id.navigation_notifications:
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
