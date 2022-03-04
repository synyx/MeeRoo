package de.synyx.android.meeroo.screen.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import android.view.MenuItem;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.preferences.PreferencesServiceImpl;
import de.synyx.android.meeroo.screen.login.LoginActivity;

import java.util.Map;

import static de.synyx.android.meeroo.util.functional.FunctionUtils.mapToArray;
import static de.synyx.android.meeroo.util.functional.FunctionUtils.toArray;
import static de.synyx.android.meeroo.util.functional.FunctionUtils.toMap;


public class SettingsActivity extends AppCompatPreferenceActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            (preference, value) -> {
                String stringValue = value.toString();

                if (preference instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) preference;
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setupActionBar();
        setGeneralFragment();
    }


    private void setGeneralFragment() {

        this.getFragmentManager()
                .beginTransaction().replace(android.R.id.content, new GeneralPreferenceFragment()).commit();
    }


    private void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onIsMultiPane() {

        return false;
    }


    @Override
    protected boolean isValidFragment(String fragmentName) {

        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);

            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(setValuesForDefaultRoom());

            Preference button = findPreference("logout");
            button.setOnPreferenceClickListener(this::logout);
        }

        private boolean logout(Preference preference) {

            Registry.get(PreferencesService.class).logout();
            restartApplication();
            return true;
        }

        private void restartApplication() {

            Intent loginActivityIntent = new Intent(getContext(), LoginActivity.class);
            int pendingIntentId = 123; // id not needed
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), pendingIntentId, loginActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            final int delay = 150;
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, pendingIntent);
            System.exit(0);
        }


        @NonNull
        private ListPreference setValuesForDefaultRoom() {

            ListPreference defaultRoomPref = (ListPreference) findPreference("defaultRoom");
            MultiSelectListPreference hiddenRooms = (MultiSelectListPreference) findPreference("hidden_rooms");

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


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();

            if (id == android.R.id.home) {
                getActivity().onBackPressed();

                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
}
