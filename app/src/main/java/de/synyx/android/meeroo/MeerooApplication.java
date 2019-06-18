package de.synyx.android.meeroo;

import android.app.Application;

import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import de.synyx.android.meeroo.config.MainConfig;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.preferences.PreferencesServiceImpl;
import de.synyx.android.meeroo.screen.login.LoginConfig;
import de.synyx.android.meeroo.util.proxy.ProxyConfig;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class MeerooApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        initConfig();
    }


    private void initConfig() {

        DefaultConfig.init();
        MainConfig.init(this);
        ProxyConfig.init(this);
        LoginConfig.init();
    }
}
