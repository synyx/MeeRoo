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


/**
 * @author  Max Dobler - dobler@synyx.de
 */
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

        MeetingRoom meetingRoom = new MeetingRoom();
        assertThat(meetingRoom.isAvailable()).isTrue();
    }


    @Test
    public void isUnavailable() {

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW));

        assertThat(meetingRoom.isAvailable()).isFalse();
    }


    @Test
    public void getTimeUntilNextMeeting_ReturnNullIfNoReservationForThisDay() {

        MeetingRoom meetingRoom = new MeetingRoom();
        assertThat(meetingRoom.getTimeUntilNextMeeting()).isNull();
    }


    @Test
    public void getTimeUntilNextMeeting() {

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusMinutes(10)));

        assertThat(meetingRoom.getTimeUntilNextMeeting()).isEqualTo(standardMinutes(10));
    }


    @Test
    public void timeUntilAvailableZeroIfCurrentlyAvailable() {

        MeetingRoom meetingRoom = new MeetingRoom();
        assertThat(meetingRoom.getTimeUntilAvailable()).isEqualTo(Duration.ZERO);
    }


    @Test
    public void timeUntilAvailable() {

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW));
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusHours(1)));

        // This reservation should not relevant, because there is a break to the previous one.
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusHours(2).plusMillis(1)));

        assertThat(meetingRoom.getTimeUntilAvailable()).isEqualTo(Duration.standardHours(2));
    }


    @Test
    public void isNextMeetingInNearFuture_IsFalseIfNoReservations() {

        MeetingRoom meetingRoom = new MeetingRoom();
        assertThat(meetingRoom.isNextMeetingInNearFuture()).isFalse();
    }


    @Test
    public void isNextMeetingInNearFuture_IsFalseIfCurrentMeeting() {

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW));

        assertThat(meetingRoom.isNextMeetingInNearFuture()).isFalse();
    }


    @Test
    public void isNextMeetingInNearFuture_IsTrueIfNextMeetingIsOnly15minAway() {

        MeetingRoom meetingRoom = new MeetingRoom();

        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusMinutes(16)));
        assertThat(meetingRoom.isNextMeetingInNearFuture()).isFalse();

        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusMinutes(15)));
        assertThat(meetingRoom.isNextMeetingInNearFuture()).isTrue();
    }


    @Test
    public void getCurrentMeeting_ReturnNullIfNoCurrentMeeting() {

        MeetingRoom meetingRoom = new MeetingRoom();
        assertThat(meetingRoom.getCurrentMeeting()).isNull();
    }


    @Test
    public void getCurrentMeeting() {

        Reservation currentMeeting = oneHourReservationBeginningAt(NOW);

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.addReservation(new Reservation(NOW.minusHours(1), NOW.minusMillis(1)));
        meetingRoom.addReservation(currentMeeting);
        meetingRoom.addReservation(oneHourReservationBeginningAt(NOW.plusHours(1)));

        assertThat(meetingRoom.getCurrentMeeting()).isEqualTo(currentMeeting);
    }


    @Test
    public void getUpcomingReservation_NullIfNoUpcoming() {

        MeetingRoom meetingRoom = new MeetingRoom();
        assertThat(meetingRoom.getUpcomingReservation()).isNull();
    }


    @Test
    public void getUpcomingReservation() {

        Reservation upcomingReservation = oneHourReservationBeginningAt(NOW.plusMillis(1));

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.addReservation(new Reservation(NOW.minusHours(1), NOW));
        meetingRoom.addReservation(upcomingReservation);

        assertThat(meetingRoom.getUpcomingReservation()).isEqualTo(upcomingReservation);
    }
}
