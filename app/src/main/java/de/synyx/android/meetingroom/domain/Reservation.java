package de.synyx.android.meetingroom.domain;

import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.util.TimeProvider;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.List;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class Reservation extends TimeSpan implements Comparable<Reservation> {

    private TimeProvider timeProvider = Registry.get(TimeProvider.class);

    private Long eventId;
    private String title;
    private List<Attendee> attendees;

    public Reservation(DateTime begin, DateTime end) {

        super(begin, end);
    }


    public Reservation(Long eventId, String title, DateTime begin, DateTime end) {

        super(begin, end);
        this.eventId = eventId;
        this.title = title;
    }

    public static Reservation withBeginAndDuration(DateTime begin, Duration duration) {

        return new Reservation(begin, begin.withDurationAdded(duration, 1));
    }


    public static Reservation oneHourReservationBeginningAt(DateTime begin) {

        return new Reservation(begin, begin.withDurationAdded(Duration.standardHours(1), 1));
    }


    public boolean isAfter(DateTime dateTime) {

        return begin.isAfter(dateTime);
    }


    public Duration getTimeUntilBegin() {

        return new Duration(timeProvider.now(), begin);
    }


    @Override
    public int compareTo(Reservation other) {

        return this.begin.compareTo(other.begin);
    }


    public boolean isActiveAt(DateTime dateTime) {

        return isEqualOrBefore(begin, dateTime) && isEqualOrAfter(end, dateTime);
    }


    public boolean isCurrentlyActive() {

        return isActiveAt(timeProvider.now());
    }


    public boolean isCurrentOrUpcoming() {

        DateTime now = timeProvider.now();

        return isEqualOrAfter(begin, now) //
            || (begin.isBefore(now) && isEqualOrAfter(end, now));
    }


    private boolean isEqualOrAfter(DateTime dateTime, DateTime other) {

        return dateTime.isEqual(other) || dateTime.isAfter(other);
    }


    private boolean isEqualOrBefore(DateTime dateTime, DateTime other) {

        return dateTime.isEqual(other) || dateTime.isBefore(other);
    }


    public TimeSpan toTimeSpan() {

        return this;
    }


    public boolean isUpcoming() {

        return begin.isAfter(timeProvider.now());
    }


    public String getTitle() {

        return title;
    }
}
