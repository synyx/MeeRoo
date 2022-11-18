package de.synyx.android.meeroo.domain

import de.synyx.android.meeroo.config.Registry
import de.synyx.android.meeroo.domain.TimeSpan.zeroTimeSpanFrom
import de.synyx.android.meeroo.util.TimeProvider
import org.joda.time.Duration
import java.util.*
import java.util.Collections.sort
import java.util.Collections.unmodifiableList


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class Agenda {

    private val timeProvider = Registry.get<TimeProvider>(TimeProvider::class.java)

    private val reservations = ArrayList<Reservation>()

    fun getTimeUntilNextMeeting(): Duration? =
            reservations
                    .filter { it.isUpcoming }
                    .map { it.timeUntilBegin }
                    .firstOrNull()


    fun getTimeUntilAvailable(): Duration =
            if (!hasCurrentReservation()) Duration.ZERO
            else reservations
                    .filter { it.isCurrentOrUpcoming }
                    .map { it.toTimeSpan() }
                    .fold(zeroTimeSpanFrom(timeProvider.now()), sumTimeOfOngoingReservations())
                    .toDuration()


    fun isNextReservationInNearFuture(): Boolean {

        if (hasCurrentReservation()) {
            return false
        }

        val timeUntilNextMeeting = getTimeUntilNextMeeting()

        return timeUntilNextMeeting != null && timeUntilNextMeeting.standardMinutes <= 15L
    }


    fun getCurrentMeeting() = reservations.firstOrNull { it.isCurrentlyActive }


    fun getUpcomingReservation() = getReservationsToday().firstOrNull { it.isUpcoming }


    fun getSecondUpcomingReservation() =
      getReservationsToday()
                    .filter { it.isUpcoming }
                    .drop(1)
                    .firstOrNull()

    fun addReservation(reservation: Reservation) {

        this.reservations.add(reservation)
        sort<Reservation>(reservations)
    }


    fun getReservations(): List<Reservation> = unmodifiableList<Reservation>(reservations)


  fun hasCurrentReservation() = reservations.any { it.isCurrentlyActive }


  private fun sumTimeOfOngoingReservations(): (TimeSpan, TimeSpan) -> TimeSpan {

    return { available, nextTimeSpan ->
      if (nextTimeSpan.begin.isAfter(available.end)) available
      else TimeSpan(available.begin, nextTimeSpan.end)
    }
  }

  public fun getReservationsToday() = reservations.filter { it.isToday }
}
