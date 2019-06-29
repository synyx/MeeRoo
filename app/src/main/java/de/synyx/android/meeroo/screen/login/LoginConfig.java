package de.synyx.android.meeroo.screen.login;

import android.content.Context;

import androidx.annotation.NonNull;

import de.synyx.android.meeroo.config.Registry;


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
