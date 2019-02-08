package de.synyx.android.meetingroom;

import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.util.DefaultTimeProvider;
import de.synyx.android.meetingroom.util.TimeProvider;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class DefaultConfig {

    public static void init() {

        Registry.put(TimeProvider.class, new DefaultTimeProvider());
    }
}
