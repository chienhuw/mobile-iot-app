package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }
    public void toProfile(View view) {

        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("userId",SharedUser.getUser().userid);
        intent.putExtra("userType","other");
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
}