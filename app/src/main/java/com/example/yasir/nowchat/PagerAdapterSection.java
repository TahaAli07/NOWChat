package com.example.yasir.nowchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Yasir on 13-02-2018.
 */

class PagerAdapterSection extends FragmentPagerAdapter{

    public PagerAdapterSection(FragmentManager fm) {

        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                RequestsFragment requestsFragment= new RequestsFragment();
                return requestsFragment;

            case 1:
                ChatsFragment chatsFragment= new ChatsFragment();
                return chatsFragment;
            case 2:

                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return  null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public String getPageTitle(int position ){
        switch (position){
            case 0:
                return "REQUESTS";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";
            default:
                    return null;
        }


    }
}
