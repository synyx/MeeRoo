package de.synyx.android.meetingroom.data;

import de.synyx.android.meetingroom.business.calendar.RoomCalendarModel;
import de.synyx.android.meetingroom.business.calendar.RoomCalendarRepository;
import de.synyx.android.meetingroom.config.Registry;

import io.reactivex.Maybe;
import io.reactivex.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class RoomCalendarRepositoryImpl implements RoomCalendarRepository {

    private CalendarAdapter calendarAdapter;

    public RoomCalendarRepositoryImpl() {

        calendarAdapter = Registry.get(CalendarAdapter.class);
    }

    @Override
    public Observable<RoomCalendarModel> loadAllRooms() {

        return calendarAdapter.loadAllRooms();
    }


    @Override
    public Observable<RoomCalendarModel> loadVisibleRooms() {

        return calendarAdapter.loadVisibleRooms();
    }


    @Override
    public Maybe<RoomCalendarModel> loadRoom(long id) {

        return calendarAdapter.loadRoom(id);
    }
}
