package de.synyx.android.meetingroom.domain;

import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.util.TimeProvider;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static de.synyx.android.meetingroom.domain.Reservation.oneHourReservationBeginningAt;
import static de.synyx.android.meetingroom.domain.TimeProviderStub.NOW;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class AgendaTest {

    @BeforeClass
    public static void setUp() {

        Registry.putOverride(TimeProvider.class, new TimeProviderStub());
    }


    @After
    public void tearDown() {

        Registry.clearOverrides();
    }


    @Test
    public void addReservationSorted() {

        Reservation reservation1 = oneHourReservationBeginningAt(NOW);
        Reservation reservation2 = oneHourReservationBeginningAt(NOW.plusHours(1));
        Reservation reservation3 = oneHourReservationBeginningAt(NOW.plusHours(2));

        Agenda agenda = new Agenda();
        agenda.addReservation(reservation3);
        agenda.addReservation(reservation1);
        agenda.addReservation(reservation2);

        assertThat(agenda.getReservations()).containsExactly(reservation1, reservation2, reservation3);
    }
}
