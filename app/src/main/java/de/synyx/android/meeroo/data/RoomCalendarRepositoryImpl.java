package de.synyx.android.meeroo.data;

import de.synyx.android.meeroo.business.calendar.RoomCalendarModel;
import de.synyx.android.meeroo.business.calendar.RoomCalendarRepository;

import io.reactivex.Maybe;
import io.reactivex.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class RoomCalendarRepositoryImpl implements RoomCalendarRepository {

    private CalendarAdapter calendarAdapter;

    public RoomCalendarRepositoryImpl(CalendarAdapter calendarAdapter) {

        this.calendarAdapter = calendarAdapter;
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
