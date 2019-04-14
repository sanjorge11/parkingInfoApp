package com.example.parkinginfoapp;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUsers;
    private DatabaseReference mReferenceLots;
    private DatabaseReference mReferencePermits;
    private List<User> users = new ArrayList<>();
    private List<Lot> lots = new ArrayList<>();
    private List<Permit> permits = new ArrayList<>();


    public interface DataStatus_Users {
        void DataIsLoaded(List<User> users, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface DataStatus_CurrentUser {
        void DataIsLoaded(User user, String key);
        void DataIsInserted(User user);
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface DataStatus_Lots {
        void DataIsLoaded(List<Lot> lots, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface DataStatus_Permits {
        void DataIsLoaded(List<Permit> permits, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceLots = mDatabase.getReference("lots");
        mReferenceUsers = mDatabase.getReference("users");
        mReferencePermits = mDatabase.getReference("permits");
    }

    public void readCurrentUser(String uid, final DataStatus_CurrentUser dataStatus) {

        String path = "users/" + uid;
        DatabaseReference mReferenceCurrentUser = mDatabase.getReference(path);

        mReferenceCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                User user = dataSnapshot.getValue(User.class);

                dataStatus.DataIsLoaded(user, key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void insertCurrentUser(String uid, final User user, final DataStatus_CurrentUser dataStatus) {

        String path = "users/" + uid;
        DatabaseReference mReferenceCurrentUser = mDatabase.getReference("users");

        mReferenceCurrentUser.child(uid).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                        //System.out.println("success");
                        dataStatus.DataIsInserted(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        //System.out.println("failure");
                    }
                });



    }

    public void readUsers(final DataStatus_Users dataStatus) {
        mReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
                dataStatus.DataIsLoaded(users, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readLots(final DataStatus_Lots dataStatus) {
        mReferenceLots.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lots.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Lot lot = keyNode.getValue(Lot.class);
                    lots.add(lot);
                }
                dataStatus.DataIsLoaded(lots, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readPermits(final DataStatus_Permits dataStatus) {
        mReferencePermits.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                permits.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Permit permit = keyNode.getValue(Permit.class);
                    permits.add(permit);
                }
                dataStatus.DataIsLoaded(permits, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
