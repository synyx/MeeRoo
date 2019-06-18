package de.synyx.android.meeroo.screen.login;

import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.util.proxy.PermissionManager;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginPresenterFactory {

    private final PreferencesService preferencesService;
    private final CalendarModeService calendarModeService;
    private final PermissionManager permissionManager;

    public LoginPresenterFactory(PreferencesService preferencesService, CalendarModeService calendarModeService,
        PermissionManager permissionManager) {

        this.preferencesService = preferencesService;
        this.calendarModeService = calendarModeService;
        this.permissionManager = permissionManager;
    }

    public LoginContract.LoginPresenter createPresenter(LoginContract.LoginView view, LoginListener listener) {

        return new LoginPresenterImpl(view, listener, preferencesService, calendarModeService, permissionManager);
    }
}
