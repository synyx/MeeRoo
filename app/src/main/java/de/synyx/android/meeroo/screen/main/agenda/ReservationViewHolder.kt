package de.synyx.android.meeroo.screen.main.agenda

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.synyx.android.meeroo.domain.Reservation
import android.widget.TextView
import de.synyx.android.meeroo.R
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * @author  Max Dobler - dobler@synyx.de
 */
class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  fun bind(reservation: Reservation) {
    setTitle(reservation)
    setTimespan(reservation)
  }

  private fun setTitle(reservation: Reservation) {
    val reservationTitle = itemView.findViewById<TextView>(R.id.reservation_title)
    reservationTitle.text = reservation.title
  }

  private fun setTimespan(reservation: Reservation) {
    val reservationTimespan = itemView.findViewById<TextView>(R.id.reservation_timespan)
    val beginTime = formatTime(reservation.getBegin())
    val endTime = formatTime(reservation.getEnd())
    reservationTimespan.text = String.format("%s - %s", beginTime, endTime)
  }

  private fun formatTime(dateTime: DateTime): String {
    return DateTimeFormat.forPattern("HH:mm").print(dateTime)
  }
}