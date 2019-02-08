package de.synyx.android.meetingroom;

import android.app.Application;

import de.synyx.android.meetingroom.config.MainConfig;
import de.synyx.android.meetingroom.screen.login.LoginConfig;
import de.synyx.android.meetingroom.util.proxy.ProxyConfig;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class MeetingRoomApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        initConfig();
    }


    private void initConfig() {

        DefaultConfig.init();
        ProxyConfig.init(this);
        LoginConfig.init(this);
        MainConfig.init();
    }
}
