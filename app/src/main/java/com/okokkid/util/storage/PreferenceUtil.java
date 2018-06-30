package com.okokkid.util.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.okokkid.MyApp;

/**
 * author： xuyafan
 * description:
 */
public class PreferenceUtil {

    public static final String TAG_TOKEN = "token";


    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());

    private static SharedPreferences getPreferences() {
        return PREFERENCES;
    }

    public static void clearAppPreferences() {
        getPreferences()
                .edit()
                .clear()
                .apply();
    }

    public static void put(String key, String val) {
        getPreferences()
                .edit()
                .putString(key, val)
                .apply();
    }

    public static String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static String get(String key) {
        return getPreferences().getString(key, "");
    }

    public static void putInt(String key, int val) {
        getPreferences()
                .edit()
                .putInt(key, val)
                .apply();
    }

    public static int getInt(String key) {
        return getPreferences().getInt(key, 0);
    }

    public static int getInt(String key, int defVal) {
        return getPreferences().getInt(key, defVal);
    }

    public static void putBoolean(String key, boolean val) {
        getPreferences()
                .edit()
                .putBoolean(key, val)
                .apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static void remove(String key) {
        getPreferences()
                .edit()
                .remove(key)
                .apply();
    }
}
