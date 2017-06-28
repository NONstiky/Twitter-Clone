package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
/**
 * Created by mbanchik on 6/26/17.
 */

@Parcel
public class Tweet {

    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public int retweetCount;
    public int likeCount;
    public int replyCount;

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        try {
            tweet.likeCount = jsonObject.getInt("favorite_count");
        }
        catch (JSONException e){
            tweet.likeCount = 0;
        }

        try{
            tweet.retweetCount = jsonObject.getInt("retweet_count");
        }
        catch (JSONException e){
            tweet.retweetCount = 0;
        }


        return tweet;
    }
}

