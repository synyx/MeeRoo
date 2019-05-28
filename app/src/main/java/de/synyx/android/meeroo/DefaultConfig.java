package de.synyx.android.meeroo;

import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.util.DefaultTimeProvider;
import de.synyx.android.meeroo.util.TimeProvider;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class DefaultConfig {

    public static void init() {

        Registry.put(TimeProvider.class, new DefaultTimeProvider());
    }
}
