package com.google.firebase.codelab.friendlychat;

/**
 * Created by saurabh on 5/4/18.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    private Context mContext;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new UserProfileFragment();
            case 1:
                return new UserList();
            case 2:
                return new ChatList();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.profile);
            case 1:
                return mContext.getString(R.string.carpools);
            case 2:
                return mContext.getString(R.string.chats);
            default:
                return null;
        }
    }
}