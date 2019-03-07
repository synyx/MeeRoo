package de.synyx.android.meetingroom.screen.main.status;

import de.synyx.android.meetingroom.business.event.EventRepository;
import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.domain.MeetingRoom;
import de.synyx.android.meetingroom.domain.Reservation;

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

        eventRepository.updateEvent(currentMeeting.getEventId(), DateTime.now().minusMinutes(1));
    }
}
