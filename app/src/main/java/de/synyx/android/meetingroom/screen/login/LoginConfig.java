package de.synyx.android.meetingroom.screen.login;

import android.content.Context;

import android.support.annotation.NonNull;

import de.synyx.android.meetingroom.config.Registry;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginConfig {

    private LoginConfig() {

        // hide
    }

    public static void init(@NonNull Context context) {

        Registry.put(LoginPresenterFactory.class, new LoginPresenterFactory());
    }
}
