package de.synyx.android.meeroo.domain

import de.synyx.android.meeroo.domain.RoomAvailability.AVAILABLE
import de.synyx.android.meeroo.domain.RoomAvailability.RESERVED
import org.joda.time.Duration

import org.joda.time.Duration.standardMinutes


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class MeetingRoom(val calendarId: Long, val name: String) {

    private val agenda = Agenda()

    fun isAvailable(): Boolean = !agenda.hasCurrentReservation()


    fun getTimeUntilNextMeeting(): Duration? = agenda.getTimeUntilNextMeeting()


    fun getTimeUntilAvailable(): Duration = agenda.getTimeUntilAvailable()


    fun isNextMeetingInNearFuture(): Boolean = agenda.isNextReservationInNearFuture()


    fun getAvailability(): RoomAvailability {

        if (!isAvailable()) {
            return RoomAvailability.UNAVAILABLE
        }

        return if (isNextMeetingInNearFuture()) RESERVED else AVAILABLE
    }


    fun getCurrentMeeting(): Reservation? = agenda.getCurrentMeeting()


    fun getUpcomingReservation(): Reservation? = agenda.getUpcomingReservation()


    fun getReservations(): List<Reservation> = agenda.getReservations()


    fun getAvailabilityTime(): Duration? {

        val availability = getAvailability()

        val duration = if (availability == RoomAvailability.UNAVAILABLE)
            getTimeUntilAvailable()
        else
            getTimeUntilNextMeeting()

        return duration?.plus(standardMinutes(1))

    }


    fun getSecondUpcomingReserveration(): Reservation? = agenda.getSecondUpcomingReservation()


    fun addReservation(reservation: Reservation) = apply { agenda.addReservation(reservation) }


}
