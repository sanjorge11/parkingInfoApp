package com.example.parkinginfoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
    }

    public void signInClicked(View view) {

        EditText emailEdit = (EditText)findViewById(R.id.emailSignIn);
        EditText passwordEdit = (EditText)findViewById(R.id.passwordSignIn);

        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("DebugTag", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("DebugTag", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) switchToMainActivity(view);
    }


    public void backToWelcomePage(View view){
        Intent myIntent = new Intent(SignInActivity.this, WelcomeActivity.class);
        SignInActivity.this.startActivity(myIntent);
    }

    public void switchToMainActivity(View view){
        Intent myIntent = new Intent(SignInActivity.this, MainActivity.class);
        SignInActivity.this.startActivity(myIntent);
    }

}
