package br.com.developen.sig.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.evernote.android.job.JobRequest;

import java.util.Date;

import br.com.developen.sig.R;
import br.com.developen.sig.job.DownloadJob;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;

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
                    .replace(R.id.activity_settings_framelayout, new PreferencesFragment())
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

        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(
                        (preferences, key) -> {

                            Log.d("SIGESC", "Configuração " + key + " alterada.");

                            switch (key){

                                case Constants.DOWNLOAD_AUTO_PROPERTY:

                                    if (preferences.getBoolean(Constants.DOWNLOAD_AUTO_PROPERTY, true)) {

                                        Date nextExecutionAt = DownloadJob.reschedule(preferences.getBoolean(Constants.DOWNLOAD_AUTO_METERED_PROPERTY, false) ? JobRequest.NetworkType.UNMETERED : JobRequest.NetworkType.ANY);

                                        if (nextExecutionAt != null)

                                            Toast.makeText(App.getContext(), "Tarefa foi agendada" /* para " + StringUtils.formatDateTime(nextExecutionAt) */, Toast.LENGTH_LONG).show();

                                    } else {

                                        DownloadJob.finish();

                                        Toast.makeText(App.getContext(), "Tarefa foi cancelada", Toast.LENGTH_LONG).show();

                                    }

                                    break;

                                case Constants.DOWNLOAD_AUTO_METERED_PROPERTY:

                                    if (preferences.getBoolean(Constants.DOWNLOAD_AUTO_PROPERTY, true)) {

                                        DownloadJob.reschedule(preferences.getBoolean(Constants.DOWNLOAD_AUTO_METERED_PROPERTY, false) ? JobRequest.NetworkType.UNMETERED : JobRequest.NetworkType.ANY);

                                        Toast.makeText(App.getContext(), "Tarefa foi reconfigurada", Toast.LENGTH_LONG).show();

                                    }

                                    break;

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
                .replace(R.id.activity_settings_framelayout, fragment)
                .addToBackStack(null)
                .commit();

        getSupportActionBar().setTitle(pref.getTitle());

        return true;

    }


    public static class PreferencesFragment extends PreferenceFragmentCompat {

        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.preferences, rootKey);

        }

    }


}