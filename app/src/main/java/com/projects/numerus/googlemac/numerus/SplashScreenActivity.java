package com.projects.numerus.googlemac.numerus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent splashIntent = new Intent(this, NavigationActivity.class);
        startActivity(splashIntent);
        finish();
    }

}
