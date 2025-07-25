package de.synyx.android.meeroo.business.event;

import org.joda.time.DateTime;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface EventRepository {

    Observable<EventModel> loadAllEventsForRoom(long roomId);


    Maybe<Long> insertEvent(long calendarId, String title, DateTime start, DateTime end);


    boolean updateEvent(long eventId, DateTime start, DateTime end, boolean recurring);
}
