package com.example.parkinginfoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void switchToLogin(View view){
        Intent myIntent = new Intent(WelcomeActivity.this, SignInActivity.class);
        WelcomeActivity.this.startActivity(myIntent);
    }

    public void switchToSignUp(View view){
        Intent myIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
        WelcomeActivity.this.startActivity(myIntent);
    }
}
