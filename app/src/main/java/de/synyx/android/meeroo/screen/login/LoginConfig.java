package de.synyx.android.meeroo.screen.login;

import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.util.proxy.PermissionManager;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginConfig {

    private LoginConfig() {

        // hide
    }

    public static void init() {

        Registry.put(LoginPresenterFactory.class,
            new LoginPresenterFactory(Registry.get(PreferencesService.class), Registry.get(CalendarModeService.class),
                Registry.get(PermissionManager.class)));
    }
}
