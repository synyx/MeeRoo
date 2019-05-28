package de.synyx.android.meeroo.business.calendar;

import io.reactivex.Maybe;
import io.reactivex.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface RoomCalendarRepository {

    Observable<RoomCalendarModel> loadAllRooms();


    Observable<RoomCalendarModel> loadVisibleRooms();


    Maybe<RoomCalendarModel> loadRoom(long id);
}
