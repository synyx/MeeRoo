package de.synyx.android.meeroo.screen.settings;

import static de.synyx.android.meeroo.BuildConfig.VERSION_NAME;
import static de.synyx.android.meeroo.util.functional.FunctionUtils.mapToArray;
import static de.synyx.android.meeroo.util.functional.FunctionUtils.toArray;
import static de.synyx.android.meeroo.util.functional.FunctionUtils.toMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Map;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.screen.FullscreenActivity;
import de.synyx.android.meeroo.screen.login.LoginActivity;


public class SettingsActivity extends FullscreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new GeneralPreferenceFragment())
                .commit();
    }


    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            (preference, value) -> {
                String stringValue = value.toString();

                if (preference instanceof ListPreference listPreference) {
                    int index = listPreference.findIndexOfValue(stringValue);
                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                } else {
                    preference.setSummary(stringValue);
                }

                return true;
            };

    private static void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class GeneralPreferenceFragment extends PreferenceFragmentCompat {

        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_general, rootKey);
            bindPreferenceSummaryToValue(setValuesForDefaultRoom());
            Preference button = findPreference("logout");
            button.setOnPreferenceClickListener(this::logout);

            findPreference("version").setSummary(VERSION_NAME);
        }

        private boolean logout(Preference preference) {

            Registry.get(PreferencesService.class).logout();
            navigateToLogin();
            return true;
        }

        private void navigateToLogin() {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            getContext().startActivity(intent);
            getActivity().finish();
        }


        @NonNull
        private ListPreference setValuesForDefaultRoom() {

            ListPreference defaultRoomPref = findPreference("defaultRoom");
            MultiSelectListPreference hiddenRooms = findPreference("hidden_rooms");

            LoadAllRoomsUseCase loadRoomUseCase = new LoadAllRoomsUseCase();
            Map<Long, String> roomValues =
                    loadRoomUseCase.execute() //
                            .map(rooms -> toMap(rooms, MeetingRoom::getCalendarId, MeetingRoom::getName)) //
                            .blockingGet();

            String[] roomNames = toArray(roomValues.values(), String.class);
            defaultRoomPref.setEntries(roomNames);
            hiddenRooms.setEntries(roomNames);

            String[] roomCalendarIds = mapToArray(roomValues.keySet(), String::valueOf, String.class);
            defaultRoomPref.setEntryValues(roomCalendarIds);
            hiddenRooms.setEntryValues(roomCalendarIds);

            return defaultRoomPref;
        }
    }
}
