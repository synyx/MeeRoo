package de.synyx.android.meetingroom.screen.login;

import de.synyx.android.meetingroom.domain.CalendarMode;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public interface LoginListener {

    void onErrorCloseButtonClick();


    void onCalenderModeClick(CalendarMode calendarMode);
}
