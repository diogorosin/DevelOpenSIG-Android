package br.com.developen.sig.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import br.com.developen.sig.R;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {


    private static final String TITLE_TAG = "settingsActivityTitle";


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.activity_settings_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new HeaderFragment())
                    .commit();

        } else {

            getSupportActionBar().setTitle(savedInstanceState.getCharSequence(TITLE_TAG));

        }

        getSupportFragmentManager().addOnBackStackChangedListener(
                () -> {

                    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                        getSupportActionBar().setTitle(R.string.settings);

                    }

                });

    }


    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putCharSequence(TITLE_TAG, getSupportActionBar().getTitle());

    }


    public boolean onSupportNavigateUp() {

        if (getSupportFragmentManager().popBackStackImmediate()) {

            return true;

        } else {

            finish();

            return true;

        }

    }


    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {

        final Bundle args = pref.getExtras();

        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());

        fragment.setArguments(args);

        fragment.setTargetFragment(caller, 0);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();

        getSupportActionBar().setTitle(pref.getTitle());

        return true;

    }


    public static class HeaderFragment extends PreferenceFragmentCompat {

        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.header_preferences, rootKey);

        }

    }


    public static class SyncFragment extends PreferenceFragmentCompat {

        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.sync_preferences, rootKey);

        }

    }


}