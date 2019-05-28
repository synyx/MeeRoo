package de.synyx.android.meeroo.screen.main.status;

import android.support.annotation.NonNull;

import de.synyx.android.meeroo.business.event.EventRepository;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.domain.Reservation;

import org.joda.time.DateTime;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class EndNowUseCase {

    private final EventRepository eventRepository;

    public EndNowUseCase() {

        eventRepository = Registry.get(EventRepository.class);
    }

    public void execute(MeetingRoom room) {

        Reservation currentMeeting = room.getCurrentMeeting();

        DateTime end = setEndToOneMinuteInThePast(currentMeeting);
        eventRepository.updateEvent(currentMeeting.getEventId(), currentMeeting.begin, end,
            currentMeeting.isRecurring());
    }


    /**
     * New end time of the reservation is set to now minus one minute, so the reservation is not visible in the view.
     * If the reservation is not one minute old the end time is set equal to the start time.
     */
    @NonNull
    private DateTime setEndToOneMinuteInThePast(Reservation currentMeeting) {

        DateTime end = DateTime.now().minusMinutes(1);

        if (end.isBefore(currentMeeting.begin)) {
            end = new DateTime(currentMeeting.begin);
        }

        return end;
    }
}
