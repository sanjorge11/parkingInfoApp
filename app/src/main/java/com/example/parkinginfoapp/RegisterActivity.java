package com.example.parkinginfoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private Permit SelectedPermit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        final Spinner spinnerPermits = findViewById(R.id.permitsRegisterSpinner);
        new FirebaseDatabaseHelper().readPermits(new FirebaseDatabaseHelper.DataStatus_Permits() {
            @Override
            public void DataIsLoaded(List<Permit> permits, List<String> keys) {

                List<String> permitStrings = new ArrayList<>();
                for(int i=0; i<permits.size(); i++) {
                    permitStrings.add(permits.get(i).permit_type);
                }

                Collections.sort(permitStrings);
                permitStrings.add(0,"None");

                ArrayAdapter<String> adapterPermits = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, permitStrings);
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

    public void registerClicked(View view) { //need validation
        EditText emailEdit = (EditText)findViewById(R.id.emailRegister);
        EditText passwordEdit = (EditText)findViewById(R.id.passwordRegister);

        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("DebugTag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            User newUser = new User();
                            String email = ((EditText) findViewById(R.id.emailRegister)).getText().toString().trim();
                            String firstName = ((EditText) findViewById(R.id.firstNameRegister)).getText().toString().trim();
                            String lastName = ((EditText) findViewById(R.id.lastNameRegister)).getText().toString().trim();

                            newUser.setEmail(email);
                            newUser.setFirstName(firstName);
                            newUser.setLastName(lastName);

                            ArrayList<String> permitList = new ArrayList<>();
                            if(SelectedPermit != null) {
                                permitList.add(SelectedPermit.getPermit_type());
                                newUser.setPermits(permitList);
                            } else {
                                permitList.add("None");
                                newUser.setPermits(permitList);
                            }

                            newUser.setType("None");

                            new FirebaseDatabaseHelper().insertCurrentUser(user.getUid(), newUser, new FirebaseDatabaseHelper.DataStatus_CurrentUser() {
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



                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("DebugTag", "createUserWithEmail:failure", task.getException());
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
        Intent myIntent = new Intent(RegisterActivity.this, WelcomeActivity.class);
        RegisterActivity.this.startActivity(myIntent);
    }

    public void switchToMainActivity(View view){
        Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(myIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        System.out.println(text);

        SelectedPermit = new Permit(text);

        System.out.println(parent);
        System.out.println();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println(parent);
        System.out.println();
    }
}
