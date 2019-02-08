package de.synyx.android.meetingroom.preferences;

import android.content.SharedPreferences;

import java.util.Set;

import static java.util.Collections.emptySet;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class PreferencesServiceImpl implements PreferencesService {

    private static final String PREF_RESERVATION_ACCOUNT = "reservationAccount";
    private static final String PREF_ADDRESS_BOOK_OPTION = "addressBookOption";

    private static final String PREFERENCES_CALENDAR_MODE = "calendarMode";
    private static final String PREF_USER_ACCOUNT = "calendarAccount"; // TODO rename to "userAccount"
    private static final String PREF_ACCOUNT_TYPE = "accountType";
    private SharedPreferences sharedPreferences;
    private SharedPreferences defaultPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesServiceImpl(SharedPreferences legacyPrefs, SharedPreferences defaultPreferences) {

        this.sharedPreferences = legacyPrefs;
        this.defaultPreferences = defaultPreferences;
        this.editor = this.sharedPreferences.edit();
    }

    @Override
    public boolean isLoggedIn() {

        final String selectedAccount = sharedPreferences.getString(PREF_RESERVATION_ACCOUNT, "");
        boolean addressBookOption = sharedPreferences.getBoolean(PREF_ADDRESS_BOOK_OPTION, false);

        return !selectedAccount.equals("") || addressBookOption;
    }


    @Override
    public void saveLoginAccount(String reservationAccount, String userAccount) {

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(PREF_RESERVATION_ACCOUNT, reservationAccount);

        edit.putString(PREF_USER_ACCOUNT, userAccount);
        edit.apply();
    }


    @Override
    public void saveLoginAccountAndType(String reservationAccount, String userAccount, String accountType) {

        editor.putString(PREF_RESERVATION_ACCOUNT, reservationAccount);

        editor.putString(PREF_ACCOUNT_TYPE, accountType);

        editor.putString(PREF_USER_ACCOUNT, userAccount);
        editor.apply();
    }


    @Override
    public void saveCalendarMode(String calendarMode) {

        editor.putString(PREFERENCES_CALENDAR_MODE, calendarMode);
        editor.apply();
    }


    @Override
    public String getSelectedCalenderMode() {

        return sharedPreferences.getString(PREFERENCES_CALENDAR_MODE, "");
    }


    @Override
    public String getUserAccountName() {

        return sharedPreferences.getString(PREF_USER_ACCOUNT, "");
    }


    @Override
    public String getUserAccountType() {

        return sharedPreferences.getString(PREF_ACCOUNT_TYPE, "");
    }


    @Override
    public Long getCalendarIdOfDefaultRoom() {

        String calendarId = defaultPreferences.getString("defaultRoom", "0");

        return Long.valueOf(calendarId);
    }


    @Override
    public Set<String> getHiddenRoomIds() {

        return defaultPreferences.getStringSet("hidden_rooms", emptySet());
    }
}
