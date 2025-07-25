package de.synyx.android.meeroo.screen.settings;

import androidx.annotation.NonNull;

import de.synyx.android.meeroo.business.calendar.RoomCalendarModel;
import de.synyx.android.meeroo.business.calendar.RoomCalendarRepository;
import de.synyx.android.meeroo.business.event.EventModel;
import de.synyx.android.meeroo.business.event.EventRepository;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.domain.Reservation;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class LoadAllRoomsUseCase {

    private final RoomCalendarRepository roomCalendarRepository;
    private final EventRepository eventRepository;

    public LoadAllRoomsUseCase() {

        roomCalendarRepository = Registry.get(RoomCalendarRepository.class);
        eventRepository = Registry.get(EventRepository.class);
    }

    public Single<List<MeetingRoom>> execute() {

        return
            roomCalendarRepository.loadAllRooms() //
            .map(this::toMeetingRoom) //
            .flatMapSingle(this::addReservations) //
            .collect(ArrayList::new, List::add);
    }


    @NonNull
    private MeetingRoom toMeetingRoom(RoomCalendarModel roomCalendar) {

        return new MeetingRoom(roomCalendar.getCalendarId(), roomCalendar.getName());
    }


    private Single<MeetingRoom> addReservations(MeetingRoom meetingRoom) {

        return
            loadEventsFor(meetingRoom) //
            .map(this::toReservation) //
            .collectInto(meetingRoom, MeetingRoom::addReservation);
    }


    @NonNull
    private Reservation toReservation(EventModel event) {

        return new Reservation(event.getId(), event.getName(), event.getBegin(), event.getEnd(), event.isRecurring());
    }


    private Observable<EventModel> loadEventsFor(MeetingRoom meetingRoom) {

        return eventRepository.loadAllEventsForRoom(meetingRoom.getCalendarId());
    }
}
