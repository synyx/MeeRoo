package de.synyx.android.meetingroom.domain;

import android.support.annotation.NonNull;

import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.util.TimeProvider;

import io.reactivex.functions.BiFunction;

import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.List;

import static de.synyx.android.meetingroom.domain.TimeSpan.zeroTimeSpanFrom;

import static io.reactivex.Observable.fromIterable;

import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class Agenda {

    private TimeProvider timeProvider = Registry.get(TimeProvider.class);

    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {

        this.reservations.add(reservation);
        sort(reservations);
    }


    public Duration getTimeUntilNextMeeting() {

        return
            fromIterable(reservations) //
            .filter(Reservation::isUpcoming) //
            .firstElement() //
            .map(Reservation::getTimeUntilBegin) //
            .blockingGet();
    }


    public List<Reservation> getReservations() {

        return unmodifiableList(reservations);
    }


    public boolean hasCurrentReservation() {

        return fromIterable(reservations) //
            .any(Reservation::isCurrentlyActive) //
            .blockingGet();
    }


    public Duration getTimeUntilAvailable() {

        if (!hasCurrentReservation()) {
            return Duration.ZERO;
        }

        return
            fromIterable(reservations) //
            .filter(Reservation::isCurrentOrUpcoming) //
            .map(Reservation::toTimeSpan) //
            .reduce(zeroTimeSpanFrom(timeProvider.now()), sumTimeOfOngoingReservations()) //
            .map(TimeSpan::toDuration) //
            .blockingGet();
    }


    @NonNull
    private BiFunction<TimeSpan, TimeSpan, TimeSpan> sumTimeOfOngoingReservations() {

        return
            (available, nextTimeSpan) -> {
            if (nextTimeSpan.begin.isAfter(available.end)) {
                return available;
            }

            return new TimeSpan(available.begin, nextTimeSpan.end);
        };
    }


    public boolean isNextReservationInNearFuture() {

        if (hasCurrentReservation()) {
            return false;
        }

        Duration timeUntilNextMeeting = getTimeUntilNextMeeting();

        if (timeUntilNextMeeting == null) {
            return false;
        }

        return timeUntilNextMeeting.getStandardMinutes() <= 15L;
    }


    public Reservation getCurrentMeeting() {

        return fromIterable(reservations) //
            .filter(Reservation::isCurrentlyActive) //
            .firstElement() //
            .blockingGet();
    }


    public Reservation getUpcomingReservation() {

        return fromIterable(reservations) //
            .filter(Reservation::isUpcoming) //
            .firstElement() //
            .blockingGet();
    }
}
