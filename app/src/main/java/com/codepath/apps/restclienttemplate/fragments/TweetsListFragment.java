package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by mbanchik on 7/3/17.
 */

public abstract class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {
    public interface TweetSelectedListener {
        // handle tweet selection
        public void onTweetSelected(Tweet tweet);
    }

    abstract protected void populateTimeline();
    private final int COMPOSE_REQUEST_CODE = 20;
    private final int DETAILS_REQUEST_CODE = 21;
    private final int REPLY_REQUEST_CODE = 22;
    private int position;
    EndlessRecyclerViewScrollListener scrollListener;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;
    TwitterClient client;

    // inflation happens inside onCreateView

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        client = TwitterApp.getRestClient();
        // find the RecyclerView
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        // init the arraylist (data)
        tweets = new ArrayList<>();
        // construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets, this);
        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvTweets.setLayoutManager(linearLayoutManager);
        // set the adapter
        rvTweets.setAdapter(tweetAdapter);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list\
                Tweet max_id_tweet = tweets.get(tweets.size()-1);
                loadNextDataFromApi(max_id_tweet.uid -1);
            }
        };

        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.twitter_blue);
        populateTimeline();
        return v;

    }

    public TweetAdapter getTweetAdapter() {
        return tweetAdapter;
    }

    public void addItems(JSONArray response) {
        //Log.d("TwitterClient", response.toString());
        // iterate through the JSON Array
        // for each entry, deserialize the JSON Object
        for (int i = 0; i < response.length(); i++) {
            // convert each object to a tweet model
            // add that Tweet model our data source
            // notify the adapter that we've added an item
            Tweet tweet = null;
            try {
                tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    public void loadNextDataFromApi(long offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.

        //  --> Deserialize and construct new model objects from the API response

        //  --> Append the new data objects to the existing set of items inside the array of items

        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

    }

    public void fetchTimelineAsync(int page) {

    }

    public void updateTweets(Tweet tweet, int CURRENT_CODE,int position) {
        if(CURRENT_CODE ==  COMPOSE_REQUEST_CODE || CURRENT_CODE == REPLY_REQUEST_CODE){

            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);

        }

        else if (CURRENT_CODE == DETAILS_REQUEST_CODE ){
            tweets.remove(position);
            tweets.add(position,tweet);
            tweetAdapter.notifyItemChanged(position);
        }
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
    }


}
