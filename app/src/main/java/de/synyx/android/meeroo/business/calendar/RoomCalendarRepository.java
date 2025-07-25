package de.synyx.android.meeroo.business.calendar;


import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface RoomCalendarRepository {

    Observable<RoomCalendarModel> loadAllRooms();


    Observable<RoomCalendarModel> loadVisibleRooms();


    Maybe<RoomCalendarModel> loadRoom(long id);
}
