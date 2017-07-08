package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.SearchView;


import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener{

    private final int COMPOSE_REQUEST_CODE = 20;
    private final int DETAILS_REQUEST_CODE = 21;
    private final int REPLY_REQUEST_CODE = 22;
    private SwipeRefreshLayout swipeContainer;

//    MenuView.ItemView miCompose;
    MenuView.ItemView miProfile;
    // Instance of the progress action-view
    MenuItem miActionProgressItem;

    FloatingActionButton fabCompose;
    TweetsPagerAdapter tweetsPagerAdapter;
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline);

        // get the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the adapter for the pager
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(),this);
        vpPager.setAdapter(tweetsPagerAdapter);
        // setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        fabCompose = (FloatingActionButton) findViewById(R.id.fabCompose);
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(i,COMPOSE_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        miCompose = (MenuView.ItemView) findViewById(R.id.miCompose);
        miProfile = (MenuView.ItemView) findViewById(R.id.miProfile);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.collapseActionView(searchItem);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                i.putExtra("q",query);
                startActivity(i);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Expand the search view and request focus
        searchItem.expandActionView();
        searchView.requestFocus();
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);

        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }
    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                //TODO Profile
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check request code and result code first
        if (resultCode == RESULT_OK && (requestCode == COMPOSE_REQUEST_CODE || requestCode == REPLY_REQUEST_CODE)) {
            // Use data parameter
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra(Tweet.class.getName()));
            int position = vpPager.getCurrentItem();
            TweetsListFragment currentFragment = (TweetsListFragment) tweetsPagerAdapter.getRegisteredFragment(position);
            currentFragment.updateTweets(tweet,requestCode,0);

        }

        else if (resultCode == RESULT_OK && requestCode == DETAILS_REQUEST_CODE) {
            // Use data parameter
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(Tweet.class.getSimpleName()));
            int p = data.getIntExtra("position",-1);
            int position = vpPager.getCurrentItem();
            TweetsListFragment currentFragment = (TweetsListFragment) tweetsPagerAdapter.getRegisteredFragment(position);
            currentFragment.updateTweets(tweet,requestCode,p);

        }
    }

    public void OnProfileView(MenuItem item) {
        // launch the profile view
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
    // Toast.makeText(this,tweet.body,Toast.LENGTH_SHORT).show();
    }

}
