package com.google.firebase.codelab.friendlychat;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Semaphore;

import static android.content.ContentValues.TAG;

/**
 * Created by saurabh on 5/3/18.
 */

 public class SharedUser{
    private static User user;
    static Semaphore sem = new Semaphore(0);
    public static User getUser(final String userid){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String mUsersChild = "users";
        mDatabase.child(mUsersChild).child(userid).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if (user == null) {

                        Log.e(TAG, "User " + userid + " is NULL UNIQUE");
                    } else {
                        Log.d(TAG, "Retrieved user " + userid + " with name " + user.username);
                    }
                    sem.release();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            }
        );
        try{
            sem.acquire();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return user;

    }
    public static User getUser(){
        return user;
    }
    public static void setUser(User user){SharedUser.user=user;}
}
