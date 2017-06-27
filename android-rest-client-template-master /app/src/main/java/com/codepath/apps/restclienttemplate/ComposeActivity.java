package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.R.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ComposeActivity extends AppCompatActivity {
    EditText etComposeTweet;
    TextView tvCharacterCount;
    Button btnComposeTweet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        EditText etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);

        setUpTextChangedListener();

    }

    public void setUpTextChangedListener(){

        etComposeTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                int chars = etComposeTweet.getText().length();
                int chars_available = 140 - chars;
                if(chars_available < 0) {
                    tvCharacterCount.setTextColor(Integer.parseInt("@colors/medium_red"));
                    // TODO Don't allow btnComposeTweet to be pressed
                }
                tvCharacterCount.setText(chars_available);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
                int chars = etComposeTweet.getText().length();
                int chars_available = 140 - chars;
                if(chars_available < 0) {
                    tvCharacterCount.setTextColor(Integer.parseInt("@colors/medium_red"));
                    // TODO Don't allow btnComposeTweet to be pressed
                }
                tvCharacterCount.setText(chars_available);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                int chars = etComposeTweet.getText().length();
                int chars_available = 140 - chars;
                if(chars_available < 0) {
                    tvCharacterCount.setTextColor(Integer.parseInt("@colors/medium_red"));
                    // TODO Don't allow btnComposeTweet to be pressed
                }
                tvCharacterCount.setText(chars_available);
            }
        });
    }

}
