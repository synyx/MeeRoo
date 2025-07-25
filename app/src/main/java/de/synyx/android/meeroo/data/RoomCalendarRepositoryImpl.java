package de.synyx.android.meeroo.data;

import de.synyx.android.meeroo.business.calendar.RoomCalendarModel;
import de.synyx.android.meeroo.business.calendar.RoomCalendarRepository;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class RoomCalendarRepositoryImpl implements RoomCalendarRepository {

    private final CalendarAdapter calendarAdapter;

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
