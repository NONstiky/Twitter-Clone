package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.codepath.apps.restclienttemplate.Utilities.NumberFormatter;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        String screenName;
        if(tweet != null){
            screenName = tweet.user.screenName;

        }
        else {
            screenName = getIntent().getStringExtra("screen_name");
        }

        // create the user fragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
        // display the user timeline fragment inside

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer,userTimelineFragment);
        // commit
        ft.commit();

        client = TwitterApp.getRestClient();
        if(tweet == null){
            fetchThisUser();
        }
        else{
            fetchOtherUser(tweet);
        }
    }

    private void fetchThisUser(){
        client.getUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // deserialize the User object
                User user = null;
                try {
                    user = User.fromJSON(response);
                    // set the title of the Action bar based on User screenName
                    getSupportActionBar().setTitle("@" + user.screenName);
                    // populate the user headline
                    populateUserHeadline(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void fetchOtherUser(Tweet tweet){
        client.getOtherUserInfo(tweet.user.screenName,tweet.user.uid, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // deserialize the User object
                User user = null;
                try {
                    user = User.fromJSON(response);
                    // set the title of the Action bar based on User screenName
                    getSupportActionBar().setTitle("@" + user.screenName);
                    // populate the user headline
                    populateUserHeadline(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void populateUserHeadline(User user){
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.name);

         tvTagline.setText(user.tagLine);
         tvFollowers.setText(NumberFormatter.format((long)user.followersCount, 0) );
         tvFollowing.setText(NumberFormatter.format((long)user.followingCount, 0) );
        // load profile image with Glide
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);
    }
}
