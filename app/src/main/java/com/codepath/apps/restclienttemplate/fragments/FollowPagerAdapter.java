package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by mbanchik on 7/7/17.
 */

public class FollowPagerAdapter extends FragmentPagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private String tabTitles[] = new String[]{"User Profile","Following","Followers"};
    private Context context;

    public FollowPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    // return the total # of fragment

    @Override
    public int getCount() {
        return 3;
    }

    // return the fragment to use depending on the position

    @Override
    public Fragment getItem(int position) {

        if (position == 0){
            UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
            userTimelineFragment.setPosition(0);
            return userTimelineFragment;
        }

        else if (position == 1) {
            FollowingFragment followingFragment = new FollowingFragment();
            followingFragment.setPosition(1);
            return followingFragment;
        }
        else if(position == 2){
            FollowersFragment followersFragment = new FollowersFragment();
            followersFragment.setPosition(2);
            return followersFragment;
        }
        else{
            return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)  super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }


    // return title

    @Override
    public CharSequence getPageTitle(int position) {
        // generate title based on item position
        return tabTitles[position];
    }
}
