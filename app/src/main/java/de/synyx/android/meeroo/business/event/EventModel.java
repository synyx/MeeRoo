package de.synyx.android.meeroo.business.event;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

import de.synyx.android.meeroo.domain.Attendee;


/**
 * @author Max Dobler - dobler@synyx.de
 */
public class EventModel implements Comparable<EventModel> {

    private final Long id;
    private final String name;
    private final DateTime begin;
    private final DateTime end;
    private final Duration duration;
    private int status;
    private List<Attendee> attendees = new ArrayList<>();

    public EventModel(Long id, String name, DateTime begin, DateTime end, Duration duration, int status) {

        this.id = id;
        this.name = name;
        this.begin = begin;
        this.end = end;
        this.duration = duration;
        this.status = status;
    }

    public Long getId() {

        return id;
    }


    public String getName() {

        return name;
    }


    public boolean isCurrent() {

        DateTime now = DateTime.now();

        return begin.isBefore(now) && end.isAfter(now);
    }


    public boolean isNextUpcoming() {

        return isSameDay() && begin.isAfter(DateTime.now());
    }


    private boolean isSameDay() {

        return LocalDate.now().isEqual(begin.toLocalDate());
    }


    public Period getRemainingTime() {

        return new Period(DateTime.now(), end);
    }


    public Period getTimeUntilBegin() {

        return new Period(DateTime.now(), begin);
    }

    public int getStatus() {
        return status;
    }

    @Override
    public int compareTo(EventModel other) {

        return begin.compareTo(other.begin);
    }


    public DateTime getBegin() {

        return begin;
    }


    public DateTime getEnd() {

        return end;
    }


    public void addAttendee(Attendee attendee) {

        this.attendees.add(attendee);
    }


    public boolean hasAttendeeCanceled(String attendeeName) {

        for (Attendee attendee : attendees) {
            if (attendee.getName().equals(attendeeName) && attendee.getStatus() == 2) {
                return true;
            }
        }

        return false;
    }


    public Duration getDuration() {

        return duration;
    }


    public boolean isRecurring() {

        return duration != null;
    }
}
