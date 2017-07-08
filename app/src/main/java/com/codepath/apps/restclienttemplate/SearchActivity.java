package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.codepath.apps.restclienttemplate.fragments.SearchTweetsFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class SearchActivity extends AppCompatActivity {
    TwitterClient client;
    RecyclerView rvTweets;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    SearchTweetsFragment searchTweetsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        client = TwitterApp.getRestClient();
        Intent i = getIntent();
        String q = i.getStringExtra("q");
        searchTweetsFragment = SearchTweetsFragment.newInstance(q);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer,searchTweetsFragment);
        // commit
        ft.commit();

    }
}
