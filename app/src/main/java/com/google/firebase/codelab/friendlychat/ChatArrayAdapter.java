package com.google.firebase.codelab.friendlychat;

/**
 * Created by saurabh on 5/4/18.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<User> {
    private final Context context;
    private final List<User> users;
    public ChatArrayAdapter(Context context, List<User> users) {
        super(context, -1, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.chat_layout_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(users.get(position).username);
        // change the icon for Windows and iPhone
        String url = users.get(position).profilePhoto;
        Glide.with(context).load(url).into(imageView);


        return rowView;
    }
}