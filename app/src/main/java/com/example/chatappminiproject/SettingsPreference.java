package com.example.chatappminiproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.example.chatappminiproject.Fragments.SettingsFragment;

import java.util.List;



public class SettingsPreference extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Load_setting();
    }

    private void Load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        boolean chk_night = sp.getBoolean("NIGHT",false);
        RelativeLayout messageLayout = (RelativeLayout) findViewById(R.id.messageL);
        if(chk_night){

            messageLayout.setBackgroundColor(this.getResources().getColor(R.color.nightmode));
        }else if(chk_night){

            messageLayout.setBackgroundColor(this.getResources().getColor(R.color.lightmode));
        }

        CheckBoxPreference chk_night_instant = (CheckBoxPreference)findPreference("NIGHT");
        chk_night_instant.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference) {

                boolean yes = (boolean)obj;

                return true;
            }
        });

        ListPreference LP = (ListPreference) findPreference("ORIENTATION");
        String orien =



    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }

    /*@Override
    public void onBuildHeaders (List<Header> target) {

        loadHeadersFromResource(R.xml.header_preference,target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }*/
}
