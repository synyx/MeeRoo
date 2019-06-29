package de.synyx.android.meeroo.config;

import android.app.Application;

import android.content.Context;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.preferences.PreferencesServiceImpl;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class Config {

    private static final String PREFS_NAME = "prefs";
    private static Config instance;

    private PreferencesService preferencesService;

    private Config(@NonNull Context context) {

        SharedPreferences legacyPrefs = context.getSharedPreferences(PREFS_NAME, Application.MODE_PRIVATE);
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferencesService = new PreferencesServiceImpl(legacyPrefs, defaultPreferences);
    }

    @NonNull
    public static Config getInstance(Context context) {

        if (instance == null) {
            instance = new Config(context);
        }

        return instance;
    }


    public PreferencesService getPreferencesService() {

        return preferencesService;
    }


    public void setPreferencesService(PreferencesService preferencesService) {

        this.preferencesService = preferencesService;
    }
}
