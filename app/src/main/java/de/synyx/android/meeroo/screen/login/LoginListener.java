package de.synyx.android.meeroo.screen.login;

import de.synyx.android.meeroo.domain.CalendarMode;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public interface LoginListener {

    void onErrorCloseButtonClick();


    void onCalenderModeClick(CalendarMode calendarMode);
}
