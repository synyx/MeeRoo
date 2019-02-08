package de.synyx.android.meetingroom.business.account;

/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public interface AccountService {

    String[] getAccountNames();


    String getUserAccountName();


    String getUserAccountType();


    void syncCalendar();
}
