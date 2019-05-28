package de.synyx.android.meeroo.util.ui;

import android.content.Context;

import android.content.res.Configuration;

import de.synyx.android.meeroo.screen.ScreenSize;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class ScreenUtil {

    private ScreenUtil() {

        // hide
    }

    public static ScreenSize getSizeOfScreen(Context context) {

        Configuration configuration = context.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

        for (ScreenSize screenSize : ScreenSize.values()) {
            if (screenWidthDp <= screenSize.getMaxWidth()) {
                return screenSize;
            }
        }

        return ScreenSize.XLARGE;
    }
}
