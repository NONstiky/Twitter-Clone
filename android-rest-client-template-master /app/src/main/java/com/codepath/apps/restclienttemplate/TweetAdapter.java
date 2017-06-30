package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.Target;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import java.text.*;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.id.ibLike;
import static com.codepath.apps.restclienttemplate.R.id.ibRetweet;
import static com.codepath.apps.restclienttemplate.models.SampleModel_Table.id;

/**
 * Created by mbanchik on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    static List<Tweet> mTweets;
    static Context context;
    // pass in the Tweets array in the Constructor
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet,parent,false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Tweet tweet = mTweets.get(position);
        String formattedTime = TimeFormatter.getTimeDifference(tweet.createdAt);
        String twitterHandle = " @" + tweet.user.screenName;
        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimeStamp.setText(" \u00b7 " + formattedTime);
        holder.tvHandle.setText(twitterHandle);

        // SET COUNTERS
        if(tweet.likeCount >0)
            holder.tvLikeCount.setText(String.valueOf(tweet.likeCount));
        else
            holder.tvLikeCount.setText(String.valueOf(""));

        if(tweet.retweetCount >0)
            holder.tvRetweetCount.setText(String.valueOf(tweet.retweetCount));
        else
            holder.tvRetweetCount.setText(String.valueOf(""));
        // SET PROFILE IMAGES
        Glide.with(context).load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .into(holder.ivProfileImage);
        // SET RETWEET AND LIKE BUTTONS
        if(tweet.retweeted){
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }
        else{
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
        }

        // About to unfavorite
        if(tweet.favorited){
            holder.ibLike.setImageResource(R.drawable.ic_vector_heart);
        }
        // About to favorite
        else{
            holder.ibLike.setImageResource(R.drawable.ic_vector_heart_stroke);
        }

    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;

        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimeStamp;
        public TextView tvHandle;
        public TextView tvRetweetCount;
        public TextView tvLikeCount;

        public ImageButton ibReply;
        public ImageButton ibRetweet;
        public ImageButton ibLike;
        public ImageButton ibDM;


        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById look

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);

            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
            ibRetweet = (ImageButton) itemView.findViewById(R.id.ibRetweet);
            ibLike = (ImageButton) itemView.findViewById(R.id.ibLike);
            ibDM = (ImageButton) itemView.findViewById(R.id.ibDM);


            itemView.setOnClickListener(this);
            ibReply.setOnClickListener(this);
            ibRetweet.setOnClickListener(this);
            ibLike.setOnClickListener(this);
            ibDM.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // gets item position
            final int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            TwitterClient client = TwitterApp.getRestClient();
            if (position != RecyclerView.NO_POSITION) {
                final Tweet tweet = mTweets.get(position);
                final int[] id = {v.getId()};
                switch(id[0]) {
                    //******************************************************************************//
                    case R.id.ibReply:
                        Toast.makeText(v.getContext(),"REPLY",Toast.LENGTH_LONG).show();
                        break;
                    //******************************************************************************//
                    case R.id.ibRetweet:
                        if(tweet.retweeted){
                            client.retweetTweet(tweet.uid, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        Tweet newTweet = Tweet.fromJSON(response);
                                        mTweets.set(position,newTweet);
//                                        tweet.retweetCount++;
//                                        tweet.retweeted = true;
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
                                        mTweets.set(position,newTweet);
//                                        tweet.retweetCount--;
//                                        tweet.retweeted = false;
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
                        break;
                    //******************************************************************************//
                    case R.id.ibLike:
                        if(tweet.favorited){
                            client.unfavoriteTweet(tweet.uid, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        Tweet newTweet = Tweet.fromJSON(response);
                                        mTweets.set(position,newTweet);
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
                                        mTweets.set(position,newTweet);
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
                        break;
                    //******************************************************************************//

                    case R.id.ibDM:
                        Toast.makeText(v.getContext(),"DM",Toast.LENGTH_LONG).show();
                        break;
                    //******************************************************************************//

                    default:
                        // create intent for the new activity
                        Intent intent = new Intent(TweetAdapter.context, TweetDetailActivity.class);
                        // serialize the tweet using parceler, use its short name as a key
                        intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                        // show the activity
                        context.startActivity(intent);


                }
            }
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

    @Override
    public int getItemCount() {

        return mTweets.size();
    }
}
