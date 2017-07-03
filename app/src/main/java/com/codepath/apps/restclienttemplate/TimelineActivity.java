package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private final int COMPOSE_REQUEST_CODE = 20;
    private final int DETAILS_REQUEST_CODE = 21;
    private final int REPLY_REQUEST_CODE = 22;
    private SwipeRefreshLayout swipeContainer;

    MenuView.ItemView miCompose;
    MenuView.ItemView miProfile;
    // Instance of the progress action-view
    MenuItem miActionProgressItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(),this));
        // setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        miCompose = (MenuView.ItemView) findViewById(R.id.miCompose);
        miProfile = (MenuView.ItemView) findViewById(R.id.miProfile);
        return true;
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
            case R.id.miCompose:
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(i,COMPOSE_REQUEST_CODE);
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
//            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra(Tweet.class.getName()));
//            tweets.add(0, tweet);
//            notifyItemInserted(0);
//            rvTweets.scrollToPosition(0);
        }

        else if (resultCode == RESULT_OK && requestCode == DETAILS_REQUEST_CODE) {
            // Use data parameter
//            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(Tweet.class.getSimpleName()));
//            int position = data.getIntExtra("position",-1);
//            tweets.remove(position);
//            tweets.add(position,tweet);
//            tweetAdapter.notifyItemChanged(position);
        }
    }

}
