package de.synyx.android.meeroo.business.calendar;

import de.synyx.android.meeroo.domain.CalendarMode;
import de.synyx.android.meeroo.preferences.PreferencesService;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public class CalendarModeServiceImpl implements CalendarModeService {

    private final String calendarValue;
    private final String resourcesValue;
    private final String prefCalenderMode;

    public CalendarModeServiceImpl(PreferencesService preferencesService, String calendarModeString,
        String resourcesModeString) {

        this.calendarValue = calendarModeString;
        this.resourcesValue = resourcesModeString;
        this.prefCalenderMode = preferencesService.getSelectedCalenderMode();
    }

    @Override
    public String getStringCalenderMode(CalendarMode calendarMode) {

        switch (calendarMode) {
            case CALENDAR:
                return calendarValue;

            case RESOURCES:
                return resourcesValue;

            default:
                return "";
        }
    }


    @Override
    public CalendarMode getCalendarModeOfString(String calendarMode) {

        if (calendarMode.equals(calendarValue)) {
            return CalendarMode.CALENDAR;
        } else if (calendarMode.equals(resourcesValue)) {
            return CalendarMode.RESOURCES;
        } else {
            return CalendarMode.NO_SELECTED_MODE;
        }
    }


    @Override
    public String[] getCalendarModes() {

        return new String[] { calendarValue, resourcesValue };
    }


    @Override
    public CalendarMode getPrefCalenderMode() {

        return getCalendarModeOfString(prefCalenderMode);
    }
}
