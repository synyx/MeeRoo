package de.synyx.android.meeroo.screen.reservation;

import de.synyx.android.meeroo.business.event.EventRepository;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.BookingResult;

import io.reactivex.Maybe;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class AddReservationUseCase {

    private final EventRepository eventRepository;

    AddReservationUseCase() {

        this.eventRepository = Registry.get(EventRepository.class);
    }

    public BookingResult execute(Long calendarId, String eventName, LocalDate eventDate, LocalTime start,
        LocalTime end) {

        Maybe<Long> event = eventRepository.insertEvent(calendarId, eventName, eventDate.toDateTime(start),
                eventDate.toDateTime(end));

        return mapToBookingResult(event);
    }


    private BookingResult mapToBookingResult(Maybe<Long> eventId) {

        return eventId //
            .map(ignored -> BookingResult.success()) //
            .blockingGet(BookingResult.error("Error while reserving room. Please try again later."));
    }
}
