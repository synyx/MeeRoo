package de.synyx.android.meetingroom.data;

import de.synyx.android.meetingroom.business.calendar.RoomCalendarModel;

import io.reactivex.Maybe;
import io.reactivex.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface CalendarAdapter {

    Observable<RoomCalendarModel> loadAllRooms();


    Observable<RoomCalendarModel> loadVisibleRooms();


    Maybe<RoomCalendarModel> loadRoom(long id);
}
