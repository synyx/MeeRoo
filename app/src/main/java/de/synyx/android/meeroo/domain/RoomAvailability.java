package de.synyx.android.meeroo.domain;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import de.synyx.android.meeroo.R;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public enum RoomAvailability {

    AVAILABLE(R.color.room_status_available, R.string.free),
    RESERVED(R.color.room_status_reserved, R.string.reserved),
    UNAVAILABLE(R.color.room_status_unavailable, R.string.unavailable);

    @StringRes
    private int colorRes;
    @ColorRes
    private int stringRes;

    RoomAvailability(int colorRes, int stringRes) {

        this.colorRes = colorRes;
        this.stringRes = stringRes;
    }

    public int getColorRes() {

        return colorRes;
    }


    public int getStringRes() {

        return stringRes;
    }
}
