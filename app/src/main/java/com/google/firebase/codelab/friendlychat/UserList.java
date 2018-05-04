package com.google.firebase.codelab.friendlychat;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class UserList extends Fragment {
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
    List<User> userList = new ArrayList<>();
    ArrayAdapter<User> arrayAdapter;
    String dLatitude, dLongitude;
    public UserList(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_list);
        View rootView = inflater.inflate(R.layout.activity_user_list,container,false);
//        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//               switch (tab.getPosition()){
//                   case 0:
//               }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userList.clear();
        userIdList.clear();
        listView = (ListView) rootView.findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<User>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(arrayAdapter);
        //dLatitude = mDatabase.child(maddreessChild1).child("ueserAddr")
        User user = SharedUser.getUser();
        mDatabase.child(maddreessChild1).child(user.dropoffAddress).child(mUsersChild).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot child : dataSnapshot.getChildren()) {
                    HashMap<String, String> users = (HashMap<String, String>) dataSnapshot.getValue();
                    if(users==null)return;
                    for (String userId : users.keySet()) {
                        //synchronized (userIdList) {
                        userIdList.add(userId);

                    }
                //}
                for (String userId : userIdList) {
                    mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            //synchronized (userList) {
                            userList.add(user);
                            arrayAdapter.notifyDataSetChanged();

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
                User user = (User)listView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), UserProfile.class);
                intent.putExtra("userId",user.userid);
                intent.putExtra("userType","other");
                startActivity(intent);
            }
        });
        return rootView;
    }


}
