package com.myprojects.numerus.spark_000.numerus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.button;
import static com.myprojects.numerus.spark_000.numerus.R.id.button_theme1;

public class ThemesActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferencesManager prefs;

    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        /*findViewById(R.id.button_theme1).setOnClickListener(this);
        findViewById(R.id.button_theme2).setOnClickListener(this);
        findViewById(R.id.button_theme3).setOnClickListener(this);*/

        /*Button theme1_button = (Button) findViewById(R.id.button_theme1);
        theme1_button.setOnClickListener(this);

        Button theme2_button = (Button) findViewById(R.id.button_theme2);
        theme2_button.setOnClickListener(this);

        Button theme3_button = (Button) findViewById(R.id.button_theme3);
        theme3_button.setOnClickListener(this);*/


        prefs = new SharedPreferencesManager(this); //get SharedPreferencesManager instance
        int t = prefs.retrieveInt("theme", 0); //get stored theme, zero is default
        Utils.setTheme(t);  //Set the stored theme

        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_themes);

        try{
            Button theme1_button = (Button) findViewById(R.id.button_theme1);
            theme1_button.setOnClickListener(this);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Theme 1 Button cannot be loaded",
                    Toast.LENGTH_LONG).show();
        }

        try{
            Button theme2_button = (Button) findViewById(R.id.button_theme2);
            theme2_button.setOnClickListener(this);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Theme 2 Button cannot be loaded",
                    Toast.LENGTH_LONG).show();
        }

        try{
            Button theme3_button = (Button) findViewById(R.id.button_theme3);
            theme3_button.setOnClickListener(this);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Theme 3 Button cannot be loaded",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void goHome(View view) {
        Intent intent1 = new Intent(this, NavigationActivity.class);
        startActivity(intent1);
    }


    public void onClick(View v) {
        switch (v.getId())
        {
            case button_theme1:
                prefs.storeInt("theme", 0); //store default theme
                Utils.changeToTheme(this, Utils.AppTheme);
                Toast.makeText(getApplicationContext(), "Switched to Theme 1!",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.button_theme2:
                Utils.changeToTheme(this, Utils.THEME_2);
                prefs.storeInt("theme", 1); //store theme 2
                Toast.makeText(getApplicationContext(), "Switched to Theme 2!",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.button_theme3:
                Utils.changeToTheme(this, Utils.THEME_3);
                prefs.storeInt("theme", 2); //store theme 3
                Toast.makeText(getApplicationContext(), "Switched to Theme 3!",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}
