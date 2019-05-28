package de.synyx.android.meeroo.business.calendar;

import de.synyx.android.meeroo.domain.CalendarMode;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public interface CalendarModeService {

    String getStringCalenderMode(CalendarMode calendarMode);


    CalendarMode getCalendarModeOfString(String calendarMode);


    String[] getCalendarModes();


    CalendarMode getPrefCalenderMode();
}
