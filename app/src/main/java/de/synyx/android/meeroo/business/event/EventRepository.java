package de.synyx.android.meeroo.business.event;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import org.joda.time.DateTime;


/**
 * @author Max Dobler - dobler@synyx.de
 */
public interface EventRepository {

    /**
     * Load today's events for room.
     */
    Observable<EventModel> loadAllEventsForRoom(long roomId);


    /**
     * Load events for room.
     *
     * @param roomId room id
     * @param days   amount of days to query events. Pass 1 for today, to for today and tomorrow, etc.
     */
    Observable<EventModel> loadAllEventsForRoom(long roomId, int days);


    Maybe<Long> insertEvent(long calendarId, String title, DateTime start, DateTime end);


    boolean updateEvent(long eventId, DateTime start, DateTime end, boolean recurring);
}
