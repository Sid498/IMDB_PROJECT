package com.example.sid.imdb_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class Splash extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Toast.makeText(this, "WELCOME TO IMDB", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(Splash.this,MainActivity.class);
                Splash.this.startActivity(intent);
                Splash.this.finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
