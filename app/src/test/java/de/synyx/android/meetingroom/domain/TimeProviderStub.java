package de.synyx.android.meetingroom.domain;

import de.synyx.android.meetingroom.util.TimeProvider;

import org.joda.time.DateTime;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class TimeProviderStub implements TimeProvider {

    public static final DateTime NOW = new DateTime(2018, 6, 1, 10, 0);

    /**
     * @return  {@code DateTime} of 1st Juni 2018 10:00am
     */
    @Override
    public DateTime now() {

        return NOW;
    }
}
