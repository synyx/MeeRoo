package de.synyx.android.meeroo.data;

import de.synyx.android.meeroo.business.calendar.RoomCalendarModel;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface CalendarAdapter {

    Observable<RoomCalendarModel> loadAllRooms();


    Observable<RoomCalendarModel> loadVisibleRooms();


    Maybe<RoomCalendarModel> loadRoom(long id);
}
