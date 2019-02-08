package de.synyx.android.meetingroom.domain;

import org.joda.time.DateTime;


/**
 * A {@code Meeting} is happening at the moment.
 *
 * @author  Max Dobler - dobler@synyx.de
 */
class Meeting extends Reservation {

    public Meeting(DateTime begin, DateTime end) {

        super(begin, end);
    }
}
