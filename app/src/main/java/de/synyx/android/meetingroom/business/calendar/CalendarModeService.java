package de.synyx.android.meetingroom.business.calendar;

import de.synyx.android.meetingroom.domain.CalendarMode;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public interface CalendarModeService {

    String getStringCalenderMode(CalendarMode calendarMode);


    CalendarMode getCalendarModeOfString(String calendarMode);


    String[] getCalendarModes();


    CalendarMode getPrefCalenderMode();
}
