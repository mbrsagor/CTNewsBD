package me.shagor.ctnewsbd;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shagor on 6/19/2017.
 */

//Creating a class for Shared  Preference
public class SharedPreference {

    public static final String SHRD_PREFS_NAME = "CtNewsPref";
    public static final String SHRD_PREFS_KEY = "CT_Version";

    public SharedPreference() {
        super();
    }

    public void save(Context context, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(SHRD_PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit();
        editor.putString(SHRD_PREFS_KEY, text);
        editor.commit();
    }

    public String getValue(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(SHRD_PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(SHRD_PREFS_KEY, null);
        return text;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(SHRD_PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(SHRD_PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(SHRD_PREFS_NAME);
        editor.commit();
    }


}
