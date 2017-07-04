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

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codepath.apps.restclienttemplate.R.id.swipeContainer;

/**
 * Created by mbanchik on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {
    public interface TweetSelectedListener{
        // handle tweet selection
        public void onTweetSelected(Tweet tweet);
    }
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    // inflation happens inside onCreateView

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list,container,false);

        // find the RecyclerView
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweet);
        // init the arraylist (data)
        tweets = new ArrayList<>();
        // construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets,this);
        // RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        // set the adapter
        rvTweets.setAdapter(tweetAdapter);
//
//
//        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) findViewById(swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
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
//
//
//    public void fetchTimelineAsync(int page) {
//        // Send the network request to fetch the updated data
//        // `client` here is an instance of Android Async HTTP
//        // getHomeTimeline is an example endpoint.
//        if(miActionProgressItem != null)
//            showProgressBar();
//
//        TwitterApp.getRestClient().getHomeTimeline(0, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                if(miActionProgressItem != null)
//                    hideProgressBar();
//                // Remember to CLEAR OUT old items before appending in the new ones
//                fragmentTweetsList.getTweetAdapter().clear();
//                // ...the data has come back, add new items to your adapter...
//                fragmentTweetsList.addItems(response);
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//        });
//    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        ((TweetSelectedListener) getActivity()).onTweetSelected(tweet);
    }
}
