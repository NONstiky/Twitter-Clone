package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class ComposeActivity extends AppCompatActivity {
    EditText etComposeTweet;
    TextView tvCharacterCount;
    Button btnComposeTweet;
    ImageButton ibGif;
    ImageButton ibPhoto;
    ImageButton ibLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);
        btnComposeTweet = (Button) findViewById(R.id.btnComposeTweet);

//        ibGif = (ImageButton) findViewById(R.id.ibGif);
//        ibPhoto = (ImageButton) findViewById(R.id.ibPhoto);
//        ibLocation = (ImageButton) findViewById(R.id.ibLocation);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        if(tweet == null){
            setUpComposeTweetButton();
        }
        else{
            setUpReplyTweetButton(tweet);
        }

        setUpTextChangedListener();

    }

    private void setUpComposeTweetButton() {
        btnComposeTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String tweet_text = etComposeTweet.getText().toString();

                TwitterApp.getRestClient().sendTweet(tweet_text,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response);
                            // create intent for the new activity
                            Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                            // serialize the movie using parceler, use its short name as a key
                            intent.putExtra(Tweet.class.getName(), Parcels.wrap(tweet));

                            setResult(RESULT_OK, intent); // set result code and bundle data for response
                            finish(); // closes the edit activity, passes intent back to main

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
        }
        });
    }


    private void setUpReplyTweetButton(final Tweet tweet) {
        etComposeTweet.setText("@"+tweet.user.screenName + " ");
        btnComposeTweet.setText("Reply");
        btnComposeTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String tweet_text = etComposeTweet.getText().toString();

                TwitterApp.getRestClient().replyTweet(tweet_text, tweet.uid,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJSON(response);
                            // create intent for the new activity
                            Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                            // serialize the movie using parceler, use its short name as a key
                            intent.putExtra(Tweet.class.getName(), Parcels.wrap(tweet));

                            setResult(RESULT_OK, intent); // set result code and bundle data for response
                            finish(); // closes the edit activity, passes intent back to main

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        });
    }
    public void setUpTextChangedListener() {

        final TextWatcher txwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                int chars = s.length();
                int chars_available = 140 - chars;
                if(chars_available < 0) {
                    tvCharacterCount.setTextColor(Integer.parseInt("@colors/medium_red"));
                }
                tvCharacterCount.setText(String.valueOf(chars_available));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etComposeTweet.addTextChangedListener(txwatcher);

    }
}

