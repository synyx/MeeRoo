package de.synyx.android.meetingroom.screen.main.status;

import de.synyx.android.meetingroom.business.calendar.RoomCalendarRepository;
import de.synyx.android.meetingroom.business.event.EventModel;
import de.synyx.android.meetingroom.business.event.EventRepository;
import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.domain.MeetingRoom;
import de.synyx.android.meetingroom.domain.Reservation;

import io.reactivex.Maybe;
import io.reactivex.Observable;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoadRoomUseCase {

    private final RoomCalendarRepository roomCalendarRepository;
    private final EventRepository eventRepository;

    public LoadRoomUseCase() {

        roomCalendarRepository = Registry.get(RoomCalendarRepository.class);
        eventRepository = Registry.get(EventRepository.class);
    }

    public Maybe<MeetingRoom> execute(long calendarId) {

        return roomCalendarRepository.loadRoom(calendarId)
            .map(roomCalendar -> new MeetingRoom(roomCalendar.getCalendarId(), roomCalendar.getName()))
            .flatMap(this::addReservations);
    }


    private Maybe<MeetingRoom> addReservations(MeetingRoom meetingRoom) {

        return loadEventsFor(meetingRoom) //
            .map(event ->
                        new Reservation(event.getId(), event.getName(), event.getBegin(), event.getEnd(),
                            event.isRecurring())) //
            .collectInto(meetingRoom, MeetingRoom::addReservation) //
            .toMaybe();
    }


    private Observable<EventModel> loadEventsFor(MeetingRoom meetingRoom) {

        return eventRepository.loadAllEventsForRoom(meetingRoom.getCalendarId());
    }
}
