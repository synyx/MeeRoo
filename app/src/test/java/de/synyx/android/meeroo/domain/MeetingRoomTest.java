package de.synyx.android.meeroo.domain;

import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.util.TimeProvider;

import org.joda.time.Duration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static de.synyx.android.meeroo.domain.Reservation.oneHourReservationBeginningAt;
import static de.synyx.android.meeroo.domain.TimeProviderStub.NOW;

import static org.assertj.core.api.Assertions.assertThat;

import static org.joda.time.Duration.standardMinutes;


public class MeetingRoomTest {

    @BeforeClass
    public static void setUp() {

        Registry.putOverride(TimeProvider.class, new TimeProviderStub());
    }


    @AfterClass
    public static void tearDown() {

        Registry.clearOverrides();
    }


    @Test
    public void isAvailable() {

        MeetingRoom meetingRoom = createMeetingRoom();
        assertThat(meetingRoom.isAvailable()).isTrue();
    }


    private MeetingRoom createMeetingRoom() {

        return new MeetingRoom(0, "room");
    }


    @Test
    public void isUnavailable() {

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW));

        assertThat(meetingRoom.isAvailable()).isFalse();
    }


    @Test
    public void getTimeUntilNextMeeting_ReturnNullIfNoReservationForThisDay() {

        MeetingRoom meetingRoom = createMeetingRoom();
        assertThat(meetingRoom.getTimeUntilNextMeeting()).isNull();
    }


    @Test
    public void getTimeUntilNextMeeting() {

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusMinutes(10)));

        assertThat(meetingRoom.getTimeUntilNextMeeting()).isEqualTo(standardMinutes(10));
    }


    @Test
    public void timeUntilAvailableZeroIfCurrentlyAvailable() {

        MeetingRoom meetingRoom = createMeetingRoom();
        assertThat(meetingRoom.getTimeUntilAvailable()).isEqualTo(Duration.ZERO);
    }


    @Test
    public void timeUntilAvailable() {

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW));
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusHours(1)));

        // This reservation should not relevant, because there is a break to the previous one.
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusHours(2).plusMillis(1)));

        assertThat(meetingRoom.getTimeUntilAvailable()).isEqualTo(Duration.standardHours(2));
    }


    @Test
    public void timeUntilAvailable_OverlappingReservations() {

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.minusMinutes(30)));
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW));

        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusMinutes(10)));

        assertThat(meetingRoom.getTimeUntilAvailable()).isEqualTo(Duration.standardMinutes(70));
    }


    @Test
    public void isNextMeetingInNearFuture_IsFalseIfNoReservations() {

        MeetingRoom meetingRoom = createMeetingRoom();
        assertThat(meetingRoom.isNextMeetingInNearFuture()).isFalse();
    }


    @Test
    public void isNextMeetingInNearFuture_IsFalseIfCurrentMeeting() {

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW));

        assertThat(meetingRoom.isNextMeetingInNearFuture()).isFalse();
    }


    @Test
    public void isNextMeetingInNearFuture_IsTrueIfNextMeetingIsOnly15minAway() {

        MeetingRoom meetingRoom = createMeetingRoom();

        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusMinutes(16)));
        assertThat(meetingRoom.isNextMeetingInNearFuture()).isFalse();

        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusMinutes(15)));
        assertThat(meetingRoom.isNextMeetingInNearFuture()).isTrue();
    }


    @Test
    public void getCurrentMeeting_ReturnNullIfNoCurrentMeeting() {

        MeetingRoom meetingRoom = createMeetingRoom();
        assertThat(meetingRoom.getCurrentMeeting()).isNull();
    }


    @Test
    public void getCurrentMeeting() {

        Reservation currentMeeting = oneHourReservationBeginningAt(NOW);

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(new Reservation(NOW.minusHours(1), NOW.minusMillis(1)));
        meetingRoom.addReservation(currentMeeting);
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusHours(1)));

        assertThat(meetingRoom.getCurrentMeeting()).isEqualTo(currentMeeting);
    }


    @Test
    public void getUpcomingReservation_NullIfNoUpcoming() {

        MeetingRoom meetingRoom = createMeetingRoom();
        assertThat(meetingRoom.getUpcomingReservation()).isNull();
    }


    @Test
    public void getUpcomingReservation() {

        Reservation upcomingReservation = oneHourReservationBeginningAt(NOW.plusMillis(1));

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(new Reservation(NOW.minusHours(1), NOW));
        meetingRoom.addReservation(upcomingReservation);

        assertThat(meetingRoom.getUpcomingReservation()).isEqualTo(upcomingReservation);
    }


    @Test
    public void getSecondUpcomingReservation_NullIfNoSecond() {

        Reservation upcomingReservation = oneHourReservationBeginningAt(NOW.plusMillis(1));

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(new Reservation(NOW.minusHours(1), NOW));
        meetingRoom.addReservation(upcomingReservation);

        assertThat(meetingRoom.getSecondUpcomingReserveration()).isEqualTo(null);
    }


    @Test
    public void getSecondUpcomingReservation() {

        Reservation upcomingReservation = oneHourReservationBeginningAt(NOW.plusMillis(1));
        Reservation secondUpcomingReservation = oneHourReservationBeginningAt(NOW.plusHours(1).plusMillis(1));

        MeetingRoom meetingRoom = createMeetingRoom();
        meetingRoom.addReservation(new Reservation(NOW.minusHours(1), NOW));
        meetingRoom.addReservation(upcomingReservation);
        meetingRoom.addReservation(secondUpcomingReservation);

        assertThat(meetingRoom.getSecondUpcomingReserveration()).isEqualTo(secondUpcomingReservation);
    }
}
