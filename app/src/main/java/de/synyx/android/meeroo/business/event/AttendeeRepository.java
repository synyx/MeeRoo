package de.synyx.android.meeroo.business.event;

/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface AttendeeRepository {

    void insertAttendeeForEvent(String attendeeName, long eventId);
}
