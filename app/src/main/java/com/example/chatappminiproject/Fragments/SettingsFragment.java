package com.example.chatappminiproject.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.chatappminiproject.R;

public class SettingsFragment extends PreferenceFragment {

    public static final String nightmode = "NIGHT";
    public static final String orien = "ORIENTATION";
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String settings = getArguments().getString("settings");

        if(settings.equals("general")){
            addPreferencesFromResource(R.xml.general_settings_preference);
        }else if(settings.equals("about")){
            addPreferencesFromResource(R.xml.about_app_settings_preference);
        }

        /*addPreferencesFromResource(R.xml.preferences);



        preferenceChangeListener =  new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if(key.equals(nightmode)){

                    Preference nightPref = findPreference(key);
                    nightPref.setSummary(sharedPreferences.getString(key, ""));
                }
            }
        };*/
    }

    /*@Override
    public void onResume(){
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }*/
}
