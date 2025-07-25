package de.synyx.android.meeroo.data;

import de.synyx.android.meeroo.domain.Attendee;
import io.reactivex.rxjava3.core.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface AttendeeAdapter {

    Observable<Attendee> getAttendeesForEvent(long eventId);


    void insertAttendeeForEvent(long eventId, String attendeeName);
}
