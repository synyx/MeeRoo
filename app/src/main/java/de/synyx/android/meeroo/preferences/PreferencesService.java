package de.synyx.android.meeroo.preferences;

import java.util.Set;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public interface PreferencesService {

    boolean isLoggedIn();


    void saveLoginAccount(String reservationAccount, String userAccount);


    void saveLoginAccountAndType(String reservationAccount, String userAccount, String accountType);


    void saveCalendarMode(String calendarMode);


    void saveLastSelectedRoom(long calendarId);


    String getSelectedCalenderMode();


    String getUserAccountName();


    String getUserAccountType();


    Long getCalendarIdOfDefaultRoom();


    Long getCalendarIdOfLastSelectedRoom();


    Set<String> getHiddenRoomIds();


    boolean isAutoNavigationEnabled();


    int getAutoNavigationTime();
}
