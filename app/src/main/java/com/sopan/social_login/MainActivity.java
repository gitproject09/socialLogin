package com.sopan.social_login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvFacebook;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFacebook = findViewById(R.id.tvFacebook);
        if (getIntent().getExtras() != null) {
            tvFacebook.setText("Welcome to App after login from Facebook\n\n"
                    + getIntent().getExtras().getString("first_name")
                    + " " +
                    getIntent().getExtras().getString("last_name"));
        }
    }
}