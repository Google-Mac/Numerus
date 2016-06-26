package com.myprojects.numerus.spark_000.numerus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ColumnTransActivity extends AppCompatActivity {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new SharedPreferencesManager(this); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_caesar_cipher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    } //end onCreate

}
