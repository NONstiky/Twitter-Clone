package com.codepath.apps.restclienttemplate;

import android.content.res.Configuration;
import android.graphics.Movie;
import android.icu.util.ValueIterator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.id.ibRetweet;
import static com.codepath.apps.restclienttemplate.TweetAdapter.context;
import static com.codepath.apps.restclienttemplate.TweetAdapter.mTweets;

public class TweetDetailActivity extends AppCompatActivity {
    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvHandle;
    TextView tvBody;
    TextView tvCreatedAt;
    TextView tvLikeCount;
    TextView tvRetweetCount;

    public ImageButton ibReply;
    public ImageButton ibRetweet;
    public ImageButton ibLike;
    public ImageButton ibDM;


    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        tvLikeCount = (TextView) findViewById(R.id.tvLikeCount);
        tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);


        ibReply = (ImageButton) findViewById(R.id.ibReply);
        ibRetweet = (ImageButton) findViewById(R.id.ibRetweet);
        ibLike = (ImageButton) findViewById(R.id.ibLike);
        ibDM = (ImageButton) findViewById(R.id.ibDM);

        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", tweet.user.name));

        tvUserName.setText(tweet.user.name);
        tvHandle.setText("@" + tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvCreatedAt.setText(TimeFormatter.getTimeStamp(tweet.createdAt));
        tvLikeCount.setText(String.valueOf(tweet.likeCount));
        tvRetweetCount.setText(String.valueOf(tweet.retweetCount));

        // SET RETWEET AND LIKE BUTTONS
        if(tweet.retweeted){
            ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }
        else{
            ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
        }

        // About to unfavorite
        if(tweet.favorited){
            ibLike.setImageResource(R.drawable.ic_vector_heart);
        }
        // About to favorite
        else{
            ibLike.setImageResource(R.drawable.ic_vector_heart_stroke);
        }

        setImage();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setImage();
    }

    private void setImage() {
        try{
            // load image using glide
            Glide.with(context).load(tweet.user.profileImageUrl)
                    .load(tweet.user.profileImageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                    .into(ivProfileImage);
        } catch(Exception e) { e.printStackTrace();}
    }

    /**
     * SET THE CLICK LISTENERS FOR THE BUTTON ROW
     */
    public void onReplyClickDetail(View v){
        Toast.makeText(context,"REPLY",Toast.LENGTH_LONG).show();
    }
    public void onRetweetClickDetail(View v){
        TwitterClient client = TwitterApp.getRestClient();
        if(tweet.retweeted){
            client.retweetTweet(tweet.uid, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet newTweet = Tweet.fromJSON(response);
                        tweet.retweetCount++;
                        tweet.retweeted = true;
                        toggleRetweetView(newTweet);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
        else{
            client.unretweetTweet(tweet.uid, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet newTweet = Tweet.fromJSON(response);
                        tweet.retweetCount--;
                        tweet.retweeted = false;
                        toggleRetweetView(newTweet);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }
    public void onLikeClickDetail(View v){
        TwitterClient client = TwitterApp.getRestClient();
        if(tweet.favorited){
            client.unfavoriteTweet(tweet.uid, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet newTweet = Tweet.fromJSON(response);
                        tweet.likeCount--;
                        tweet.favorited = false;
                        toggleLikeView(newTweet);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
        else{
            client.favoriteTweet(tweet.uid, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Tweet newTweet = Tweet.fromJSON(response);
                        tweet.likeCount++;
                        tweet.favorited = true;
                        toggleLikeView(newTweet);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    public void onDMClickDetail(View v){
        Toast.makeText(context,"DM",Toast.LENGTH_LONG).show();

    }



    public void toggleLikeView(Tweet tweet){
        // About to unfavorite
        if(tweet.favorited){
            Toast.makeText(context,"Favorite",Toast.LENGTH_LONG).show();
            ibLike.setImageResource(R.drawable.ic_vector_heart);
        }
        // About to favorite
        else{
            Toast.makeText(context,"Unfavorite",Toast.LENGTH_LONG).show();
            ibLike.setImageResource(R.drawable.ic_vector_heart_stroke);
        }

        // Set Counts
        if(tweet.likeCount > 0){
            tvLikeCount.setText(String.valueOf(tweet.likeCount));
        }
        else{
            tvLikeCount.setText(String.valueOf(""));
        }
    }

    public void toggleRetweetView(Tweet tweet){
        // About to unretweet
        if(tweet.retweeted){
            Toast.makeText(context,"Retweet",Toast.LENGTH_LONG).show();
            ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }
        // About to retweet
        else{
            Toast.makeText(context,"Untweet",Toast.LENGTH_LONG).show();
            ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
        }

        // Set Counts
        if(tweet.retweetCount > 0){
            tvRetweetCount.setText(String.valueOf(tweet.retweetCount));
        }
        else {
            tvRetweetCount.setText(String.valueOf(""));
        }
    }

}
