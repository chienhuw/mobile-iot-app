package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import static android.content.ContentValues.TAG;

public class MainPage extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener{
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    String mUsername;
    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {

                            startEditScreen(user);
                        } else {
                            SharedUser.setUser(user);
                            Log.d(TAG, "Retrieved user " + mFirebaseUser.getUid() + " with name " + user.username);
                            setContentView(R.layout.activity_main_page);

                            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                            tabLayout.addTab(tabLayout.newTab().setText("Profile"));
                            tabLayout.addTab(tabLayout.newTab().setText("Carpools"));
                            tabLayout.addTab(tabLayout.newTab().setText("Chats"));
                            PagerAdapter adapter = new PagerAdapter
                                    (getApplicationContext(),getSupportFragmentManager());

                            viewPager.setAdapter(adapter);
                            //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                            tabLayout.setupWithViewPager(viewPager);
                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    viewPager.setCurrentItem(tab.getPosition());
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }});
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                }
            );
            // if (mFirebaseUser.getPhotoUrl() != null) {
            //     mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            // }
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void startEditScreen(User user){
        Intent intent = new Intent(MainPage.this,Register.class);
        if(user!=null) {
            intent.putExtra("userid", user.userid);
            intent.putExtra("editing", "true");
        }
        MainPage.this.startActivity(intent);
    }
    public void toProfile(View view) {

        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("userId",SharedUser.getUser().userid);
        intent.putExtra("userType","self");
        startActivity(intent);
    }
    public void toCarpools(View view){
        Intent intent = new Intent(this, UserList.class);
        startActivity(intent);
    }
    public void toChatHistory(View view){
        Intent intent = new Intent(this,ChatList.class);
        startActivity(intent);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("MainPage", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}