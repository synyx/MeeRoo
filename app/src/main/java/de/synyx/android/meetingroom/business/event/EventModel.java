package de.synyx.android.meetingroom.business.event;

import de.synyx.android.meetingroom.domain.Attendee;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class EventModel implements Comparable<EventModel> {

    private final Long id;
    private final String name;
    private final DateTime begin;
    private final DateTime end;
    private List<Attendee> attendees = new ArrayList<>();

    public EventModel(Long id, String name, DateTime begin, DateTime end) {

        this.id = id;
        this.name = name;
        this.begin = begin;
        this.end = end;
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
}
