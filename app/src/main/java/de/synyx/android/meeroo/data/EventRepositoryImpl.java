package de.synyx.android.meeroo.data;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.concurrent.ConcurrentHashMap;

import de.synyx.android.meeroo.business.event.EventModel;
import de.synyx.android.meeroo.business.event.EventRepository;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * @author Max Dobler - dobler@synyx.de
 */
public class EventRepositoryImpl implements EventRepository {

    private final EventAdapter eventAdapter;
    private final AttendeeAdapter attendeeAdapter;
    private final ConcurrentHashMap<Long, DateTime> eventEndCache;

    public EventRepositoryImpl(EventAdapter eventAdapter, AttendeeAdapter attendeeAdapter) {

        this.eventAdapter = eventAdapter;
        this.attendeeAdapter = attendeeAdapter;

        eventEndCache = new ConcurrentHashMap<>();
    }

    @Override
    public Observable<EventModel> loadAllEventsForRoom(long roomId) {

        return loadAllEventsForRoom(roomId, 1);
    }


    @Override
    public Observable<EventModel> loadAllEventsForRoom(long roomId, int days) {

        return eventAdapter.getEventsForRoom(roomId, days) //
                .map(this::setEndIfCached)
                .flatMap(loadAttendees());
    }

    private EventModel setEndIfCached(EventModel event) {

        DateTime end = eventEndCache.get(event.getId());

        if (end != null) {
            return new EventModel(event.getId(), event.getName(), event.getBegin(), end, event.getStatus(), event.isRecurring());
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
