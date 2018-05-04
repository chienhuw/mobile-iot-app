package com.google.firebase.codelab.friendlychat;

import android.support.v4.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by saurabh on 5/4/18.
 */


public class ChatList extends Fragment{
    private DatabaseReference mDatabase;
    private ListView listView;
    private List<String> userIdList =new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    public ChatList(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle){
        super.onCreate(icicle);
        View rootView = inflater.inflate(R.layout.chat_list_layout,container,false);
        //setContentView(R.layout.chat_list_layout);
        final ChatArrayAdapter adapter = new ChatArrayAdapter(getActivity().getApplicationContext(), userList);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userList.clear();
        userIdList.clear();
        mDatabase.child("activeChats").child(SharedUser.getUser().userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot child : dataSnapshot.getChildren()) {
                    HashMap<String, String> users = (HashMap<String, String>) dataSnapshot.getValue();
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
                            adapter.notifyDataSetChanged();

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

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User)listView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userid",user.userid);
                ChatList.this.startActivity(intent);
            }
        });


    return rootView;
    }
}
