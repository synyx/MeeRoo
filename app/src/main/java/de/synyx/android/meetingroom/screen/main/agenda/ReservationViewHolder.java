package de.synyx.android.meetingroom.screen.main.agenda;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.TextView;

import de.synyx.android.meetingroom.R;
import de.synyx.android.meetingroom.domain.Reservation;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import static java.lang.String.format;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class ReservationViewHolder extends RecyclerView.ViewHolder {

    ReservationViewHolder(@NonNull View itemView) {

        super(itemView);
    }

    void bind(Reservation reservation) {

        setTitle(reservation);
        setTimespan(reservation);
    }


    private void setTitle(Reservation reservation) {

        TextView reservationTitle = itemView.findViewById(R.id.reservation_title);
        reservationTitle.setText(reservation.getTitle());
    }


    private void setTimespan(Reservation reservation) {

        TextView reservationTimespan = itemView.findViewById(R.id.reservation_timespan);
        String beginTime = formatTime(reservation.getBegin());
        String endTime = formatTime(reservation.getEnd());
        reservationTimespan.setText(format("%s - %s", beginTime, endTime));
    }


    private String formatTime(DateTime dateTime) {

        return DateTimeFormat.forPattern("HH:mm").print(dateTime);
    }
}
