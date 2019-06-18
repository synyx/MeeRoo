package de.synyx.android.meeroo.data;

import de.synyx.android.meeroo.business.event.AttendeeRepository;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class AttendeeRepositoryImpl implements AttendeeRepository {

    private final AttendeeAdapter attendeeAdapter;

    public AttendeeRepositoryImpl(AttendeeAdapterImpl attendeeAdapter) {

        this.attendeeAdapter = attendeeAdapter;
    }

    @Override
    public void insertAttendeeForEvent(String attendeeName, long eventId) {

        attendeeAdapter.insertAttendeeForEvent(eventId, attendeeName);
    }
}
