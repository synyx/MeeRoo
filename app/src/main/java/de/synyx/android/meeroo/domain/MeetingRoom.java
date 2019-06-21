package de.synyx.android.meeroo.domain;

import org.joda.time.Duration;

import java.util.List;

import static org.joda.time.Duration.standardMinutes;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class MeetingRoom {

    private Long calendarId;
    private String name;
    private Agenda agenda = new Agenda();

    public MeetingRoom(long calendarId, String name) {

        this.calendarId = calendarId;
        this.name = name;
    }


    public MeetingRoom() {
    }

    public boolean isAvailable() {

        return !agenda.hasCurrentReservation();
    }


    public Duration getTimeUntilNextMeeting() {

        return agenda != null ? agenda.getTimeUntilNextMeeting() : null;
    }


    public MeetingRoom addReservation(Reservation reservation) {

        agenda.addReservation(reservation);

        return this;
    }


    public Duration getTimeUntilAvailable() {

        return agenda.getTimeUntilAvailable();
    }


    public boolean isNextMeetingInNearFuture() {

        return agenda.isNextReservationInNearFuture();
    }


    public long getCalendarId() {

        return this.calendarId;
    }


    public String getName() {

        return name;
    }


    public RoomAvailability getAvailability() {

        if (!isAvailable()) {
            return RoomAvailability.UNAVAILABLE;
        }

        return isNextMeetingInNearFuture() ? RoomAvailability.RESERVED : RoomAvailability.AVAILABLE;
    }


    public Reservation getCurrentMeeting() {

        return agenda.getCurrentMeeting();
    }


    public Reservation getUpcomingReservation() {

        return agenda.getUpcomingReservation();
    }


    public List<Reservation> getReservations() {

        return agenda.getReservations();
    }


    public Duration getAvailabilityTime() {

        RoomAvailability availability = getAvailability();

        Duration duration = availability == RoomAvailability.UNAVAILABLE ? getTimeUntilAvailable()
                                                                         : getTimeUntilNextMeeting();

        if (duration != null) {
            return duration.plus(standardMinutes(1));
        }

        return null;
    }


    public Reservation getSecondUpcomingReserveration() {

        return agenda.getSecondUpcomingReservation();
    }
}
