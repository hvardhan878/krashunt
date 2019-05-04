package com.krashunt.krashunt2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT= 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(home);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

    }
}
