package de.synyx.android.meeroo.screen.login.mvvm;

import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.util.proxy.PermissionManager;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class MVVMLoginConfig {

    private final PreferencesService preferencesService;
    private final PermissionManager permissionManager;
    private final CalendarModeService calendarModeService;

    public MVVMLoginConfig(PreferencesService preferencesService, PermissionManager permissionManager,
        CalendarModeService calendarModeService) {

        this.preferencesService = preferencesService;
        this.permissionManager = permissionManager;
        this.calendarModeService = calendarModeService;
    }

    public PreferencesService getPreferencesService() {

        return preferencesService;
    }


    public PermissionManager getPermissionManager() {

        return permissionManager;
    }


    public CalendarModeService getCalendarModeService() {

        return calendarModeService;
    }
}
