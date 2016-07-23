package com.projects.numerus.googlemac.numerus;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by spark_000 on 6/22/2016.
 */

public class Utils {

    private static int sTheme;
    public final static int AppTheme = 0;
    public final static int THEME_2 = 1;
    public final static int THEME_3 = 2;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */

    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void setTheme(int t){
        sTheme = t;
    }

    /** Set the theme of the activity, according to the configuration. */

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
                            /** TODO: Create other themes! */
            case AppTheme:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_2:
                activity.setTheme(R.style.THEME_2);
                break;
            case THEME_3:
                activity.setTheme(R.style.THEME_3);
                break;
        }
    }
}
