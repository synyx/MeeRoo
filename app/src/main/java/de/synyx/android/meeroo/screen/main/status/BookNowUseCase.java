package de.synyx.android.meeroo.screen.main.status;

import de.synyx.android.meeroo.business.event.AttendeeRepository;
import de.synyx.android.meeroo.business.event.EventRepository;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.BookingResult;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.domain.Reservation;
import de.synyx.android.meeroo.util.TimeProvider;
import io.reactivex.rxjava3.core.Maybe;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import static de.synyx.android.meeroo.domain.BookingResult.error;

import static org.joda.time.Duration.standardMinutes;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class BookNowUseCase {

    private final EventRepository eventRepository;
    private final TimeProvider timeProvider;
    private final AttendeeRepository attendeeRepository;

    BookNowUseCase() {

        eventRepository = Registry.get(EventRepository.class);
        timeProvider = Registry.get(TimeProvider.class);
        attendeeRepository = Registry.get(AttendeeRepository.class);
    }

    public BookingResult execute(long calendarId, MeetingRoom meetingRoom, Duration duration, String eventTitle) {

        Duration timeUntilNextMeeting = meetingRoom.getTimeUntilNextMeeting();

        if (timeUntilNextMeeting == null || timeUntilNextMeeting.isLongerThan(duration)) {
            DateTime start = timeProvider.now();

            return bookMeetingRoom(calendarId, eventTitle, meetingRoom.getName(), start, start.plus(duration));
        }

        if (timeUntilNextMeeting.isLongerThan(standardMinutes(15))) {
            Reservation upcomingReservation = meetingRoom.getUpcomingReservation();

            return bookMeetingRoom(calendarId, eventTitle, meetingRoom.getName(), timeProvider.now(),
                    upcomingReservation.begin);
        }

        return error("Meeting room is already reserved!");
    }


    private BookingResult bookMeetingRoom(long calendarId, String eventTitle, String attendeeName, DateTime start,
        DateTime end) {

        Maybe<Long> eventId =
            eventRepository.insertEvent(calendarId, eventTitle, start, end) //
            .doOnSuccess(id -> attendeeRepository.insertAttendeeForEvent(attendeeName, id));

        return mapToBookingResult(eventId);
    }


    private BookingResult mapToBookingResult(Maybe<Long> eventId) {

        return eventId //
                .map(ignored -> BookingResult.success()) //
            .blockingGet(BookingResult.error("Error while reserving room. Please try again later."));
    }
}
