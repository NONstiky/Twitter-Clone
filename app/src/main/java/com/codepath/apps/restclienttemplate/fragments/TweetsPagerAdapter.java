package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by mbanchik on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private String tabTitles[] = new String[]{"Home","Mentions"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }
    // return the total # of fragment

    @Override
    public int getCount() {
        return 2;
    }

    // return the fragment to use depending on the position

    @Override
    public Fragment getItem(int position) {

        if (position == 0){
            HomeTimelineFragment homeTimelineFragment = new HomeTimelineFragment();
            homeTimelineFragment.setPosition(0);
            return homeTimelineFragment;
        }

        else if (position == 1) {
            MentionsTimelineFragment mentionsTimelineFragment = new MentionsTimelineFragment();
            mentionsTimelineFragment.setPosition(1);
            return mentionsTimelineFragment;
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
