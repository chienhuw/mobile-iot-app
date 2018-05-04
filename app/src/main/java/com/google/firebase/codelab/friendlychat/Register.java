package com.google.firebase.codelab.friendlychat;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.support.annotation.NonNull;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;

public class Register extends AppCompatActivity{


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
 //   private String userid = "user1";


    private TextView mName;
    private TextView mAddress;
    private TextView mName2;
    private TextView mAddress2;
    private TextView mAttributions;
    private EditText nameText, phoneText, bioText;
    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
           // if (mFirebaseUser.getPhotoUrl() != null) {
           //     mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
           // }

        }



        // address name & address
        mName = (TextView) findViewById(R.id.nametextView);
        mAddress = (TextView) findViewById(R.id.addtextView);
        mName2 = (TextView) findViewById(R.id.nametextView2);
        mAddress2 = (TextView) findViewById(R.id.addtextView2);
        mAttributions = (TextView) findViewById(R.id.untextView);

        Button pickerButton = (Button) findViewById(R.id.pickerButton);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(Register.this);
                    startActivityForResult(intent, 0);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        Button dropButton = (Button) findViewById(R.id.dropButton);
        dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(Register.this);
                    startActivityForResult(intent, 1);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();


            final CharSequence address = place.getAddress();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }


            switch(requestCode) {
                case 0:
                    mName.setText(name);
                    mAddress.setText(address);
                    mAttributions.setText(Html.fromHtml(attributions));
                    break;

                case 1:
                    mName2.setText(name);
                    mAddress2.setText(address);
                    mAttributions.setText(Html.fromHtml(attributions));
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    // check login
    public void createUser(View view) {


        // database
        nameText = (EditText) findViewById(R.id.editText2);
        phoneText = (EditText) findViewById(R.id.editText3);
        mAddress = (TextView) findViewById(R.id.addtextView);
        mAddress2 = (TextView) findViewById(R.id.addtextView2);
        checkbox = (CheckBox) findViewById(R.id.checkBox);
        bioText = (EditText) findViewById(R.id.bioEdit);


        String name = nameText.getText().toString();
        String phone = phoneText.getText().toString();
        String pick = mAddress.getText().toString();
        String pickName = mName.getText().toString();
        String drop = mAddress2.getText().toString();
        String dropName = mName2.getText().toString();
        String bio = bioText.getText().toString();
        boolean checkCar = checkbox.isChecked();

        LatLng pickGeo = getLocationFromAddress(this,pick);
        String picklat = String.valueOf(pickGeo.latitude);
        String picklong = String.valueOf(pickGeo.longitude);

        LatLng dropGeo = getLocationFromAddress(this,drop);
        String droplat = String.valueOf(dropGeo.latitude);
        String droplong = String.valueOf(dropGeo.longitude);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        User user1 = new User(mFirebaseUser.getUid(), name, "http://www.a-star-aspirations.co.uk/wp-content/uploads/2014/03/Emotes-face-smile-icon.png",picklat,picklong, pick, pickName, drop, dropName, bio,phone, checkCar);
            mDatabase.child(mUsersChild).child(user1.userid).setValue(user1);

        //dropoff table
        mDatabase.child(maddreessChild1).child(drop).child("name").setValue(dropName);
        mDatabase.child(maddreessChild1).child(drop).child("latitude").setValue(droplat);
        mDatabase.child(maddreessChild1).child(drop).child("longitude").setValue(droplong);
        mDatabase.child(maddreessChild1).child(drop).child("users").child(mFirebaseUser.getUid()).setValue(mFirebaseUser.getUid());
        SharedUser.setUser(user1);
        //mDatabase.child(maddreessChild1).child(dropName).setValue();


        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }

    public LatLng getLocationFromAddress(Context context, String inputtedAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(inputtedAddress, 5);
            if (address == null) {
                return null;
            }
            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return resLatLng;
    }



}
