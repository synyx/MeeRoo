package de.synyx.android.meetingroom.business.event;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import org.joda.time.DateTime;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface EventRepository {

    Observable<EventModel> loadAllEventsForRoom(long roomId);


    Maybe<Long> insertEvent(long calendarId, String title, DateTime start, DateTime end);


    boolean updateEvent(long eventId, DateTime end);
}
