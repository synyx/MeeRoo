package de.synyx.android.meeroo.data;

import androidx.annotation.NonNull;

import de.synyx.android.meeroo.business.event.EventModel;
import de.synyx.android.meeroo.business.event.EventRepository;
import de.synyx.android.meeroo.config.Registry;

import io.reactivex.Maybe;
import io.reactivex.Observable;

import io.reactivex.functions.Function;

import org.joda.time.DateTime;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class EventRepositoryImpl implements EventRepository {

    private final EventAdapter eventAdapter;
    private final AttendeeAdapter attendeeAdapter;
    private final ConcurrentHashMap<Long, DateTime> eventEndCache;

    public EventRepositoryImpl() {

        eventAdapter = Registry.get(EventAdapter.class);
        attendeeAdapter = Registry.get(AttendeeAdapter.class);
        eventEndCache = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<EventModel> loadAllEventsForRoom(long roomId) {

        return eventAdapter.getEventsForRoom(roomId) //
            .map(this::setEndIfCached)
            .flatMap(loadAttendees());
    }


    private EventModel setEndIfCached(EventModel event) {

        DateTime end = eventEndCache.get(event.getId());

        if (end != null) {
            return new EventModel(event.getId(), event.getName(), event.getBegin(), end, null);
        }

        return event;
    }


    @Override
    public Maybe<Long> insertEvent(long calendarId, String title, DateTime start, DateTime end) {

        return eventAdapter.insertEvent(calendarId, start, end, title);
    }


    @Override
    public boolean updateEvent(long eventId, DateTime start, DateTime end, boolean recurring) {

        eventEndCache.put(eventId, end);

        return eventAdapter.updateEvent(eventId, start, end, recurring);
    }


    @NonNull
    private Function<EventModel, Observable<EventModel>> loadAttendees() {

        return
            event ->
                attendeeAdapter.getAttendeesForEvent(event.getId())
                .collectInto(event, EventModel::addAttendee).toObservable();
    }
}
