package com.google.firebase.codelab.friendlychat;//package edu.cmu.mobileiot.carpoolingkids;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by saurabh on 4/2/18.
 */

public class UserProfile extends Activity
        implements GoogleApiClient.OnConnectionFailedListener{

    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUsersChild = "users";
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "UserProfile";
    private String userid = "user1";
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_userprofile);
        ImageView imageViewpp=(ImageView) findViewById(R.id.profilePhoto);
        imageViewpp.setImageResource(R.drawable.profile_picture);
        ImageView imageViewFriend1=(ImageView) findViewById(R.id.imageFriend1);
        imageViewFriend1.setImageResource(R.drawable.dp1);
        ImageView imageViewFriend2=(ImageView) findViewById(R.id.imageFriend2);
        imageViewFriend2.setImageResource(R.drawable.dp2);
        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mFirebaseUser.getUid();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
               // .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user1 = new User(userid,"Saurabh Misra","http://randomurl.com","52.456","32.234","5700 Fifth","I am cool","Negley Martial Arts","4133134322" );
        //mDatabase.child(mUsersChild).child(user1.userid).setValue(user1);
        mDatabase.child(mUsersChild).child(user1.userid).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if(user == null){

                        Log.e(TAG,"User "+userid+" is NULL UNIQUE");
                    }
                    else{
                        Toast.makeText(UserProfile.this,"Retreived user "+userid +" with name "+user.username,Toast.LENGTH_LONG);
                        Log.d(TAG,"Retreived user "+userid +" with name "+user.username);
                        TextView bio = findViewById(R.id.bio);
                        bio.setText(user.bio);
                        TextView userTitle = findViewById(R.id.titleUser);
                        userTitle.setText(user.username);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            }
        );
    }
    public void startChatScreen(View view){
        Intent intent = new Intent(UserProfile.this,MainActivity.class);
        intent.putExtra("userid",user.userid);
        UserProfile.this.startActivity(intent);
        //startActivity(intent);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

}