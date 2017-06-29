package com.codepath.apps.restclienttemplate;

import android.content.res.Configuration;
import android.graphics.Movie;
import android.icu.util.ValueIterator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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

        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", tweet.user.name));

        tvUserName.setText(tweet.user.name);
        tvHandle.setText("@" + tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvCreatedAt.setText(TimeFormatter.getTimeStamp(tweet.createdAt));
        tvLikeCount.setText(String.valueOf(tweet.likeCount));
        tvRetweetCount.setText(String.valueOf(tweet.retweetCount));

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
        Toast.makeText(context,"RETWEET",Toast.LENGTH_LONG).show();

    }
    public void onLikeClickDetail(View v){
        Toast.makeText(context,"LIKE",Toast.LENGTH_LONG).show();

    }
    public void onDMClickDetail(View v){
        Toast.makeText(context,"DM",Toast.LENGTH_LONG).show();

    }

}
