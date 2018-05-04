package com.google.firebase.codelab.friendlychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class UserList extends AppCompatActivity {
    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUsersChild = "users";
    private String maddreessChild1 = "dropoff";
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "UserProfile";
    private String userAddr = "1400 Locust St, Pittsburgh, PA 15219, USA";
    ListView listView;
    List<String> userIdList = new ArrayList<>();
    List<String> userList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String dLatitude, dLongitude;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userList.clear();
        listView = (ListView) findViewById(R.id.listView);
        //dropoff->address->users
        //final Semaphore sem0 = new Semaphore(0);
        //
        arrayAdapter = new ArrayAdapter<String>(UserList.this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(arrayAdapter);
        //dLatitude = mDatabase.child(maddreessChild1).child("userAddr")
        mDatabase.child(maddreessChild1).child(userAddr).child(mUsersChild).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //String user = (String)dataSnapshot.getValue();
                    //System.out.println(value);
                    HashMap<String, String> users = (HashMap<String, String>) dataSnapshot.getValue();
                    for (String userId : users.keySet()) {
                        //synchronized (userIdList) {
                        userIdList.add(userId);

                        //}
                    }

                    //int userNumber = userIdList.size();

                }

                //sem0.release();
                //users->userid->username
                for (String userId : userIdList) {
                    mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //for (DataSnapshot child: dataSnapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            //synchronized (userList) {
                            userList.add(user.username);
                            arrayAdapter.notifyDataSetChanged();


                            //}
                            //sem.release();

                            //}

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //sem0.release();
                        }
                    });

                }
            }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //sem0.release();
                }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        /*
        try {
            sem0.acquire();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        /*
        Log.d(TAG,"Reached here");


        //final Semaphore sem = new Semaphore(1-userIdList.size());
        for (String userId: userIdList) {
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //for (DataSnapshot child: dataSnapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        //synchronized (userList) {
                            userList.add(user.username);
                        //}
                        //sem.release();

                    //}

                }




                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //sem.release();
                }
            });

        }
        /*
        try {
            sem.acquire();
        } catch (InterruptedException e) {

        }
        */
            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UserList.this, android.R.layout.simple_list_item_1, userList);
            //listView.setAdapter(arrayAdapter);

    }













}
