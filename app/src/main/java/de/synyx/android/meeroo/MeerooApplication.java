package de.synyx.android.meeroo;

import android.app.Application;

import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.config.MainConfig;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.screen.login.LoginViewModelFactory;
import de.synyx.android.meeroo.util.proxy.PermissionManager;
import de.synyx.android.meeroo.util.proxy.ProxyConfig;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class MeerooApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        // TODO: initialize services directly here
        initConfig();

        // for login
        Registry.put(LoginViewModelFactory.class,
            new LoginViewModelFactory(Registry.get(PreferencesService.class), Registry.get(PermissionManager.class),
                Registry.get(CalendarModeService.class)));
    }


    private void initConfig() {

        DefaultConfig.init();
        MainConfig.init(this);
        ProxyConfig.init(this);
    }
}
