package de.synyx.android.meeroo.util.proxy;

import android.content.Context;

import androidx.annotation.NonNull;

import de.synyx.android.meeroo.config.Registry;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public class ProxyConfig {

    private ProxyConfig() {

        // hide
    }

    public static void init(@NonNull Context context) {

        Registry.put(PermissionManager.class, new PermissionManager(context));
    }
}
