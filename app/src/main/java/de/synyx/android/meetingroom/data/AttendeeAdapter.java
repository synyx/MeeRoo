package de.synyx.android.meetingroom.data;

import de.synyx.android.meetingroom.domain.Attendee;

import io.reactivex.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface AttendeeAdapter {

    Observable<Attendee> getAttendeesForEvent(long eventId);


    void insertAttendeeForEvent(long eventId, String attendeeName);
}
