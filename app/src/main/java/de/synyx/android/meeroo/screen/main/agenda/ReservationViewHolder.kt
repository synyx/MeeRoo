package de.synyx.android.meeroo.screen.main.agenda

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import de.synyx.android.meeroo.domain.Reservation
import android.widget.TextView
import de.synyx.android.meeroo.R
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

/**
 * @author  Max Dobler - dobler@synyx.de
 */
class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  fun bind(item: AgendaListItem.ReservationItem) {
    setTitle(item.reservation)
    setTimespan(item.reservation)
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

class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  fun bind(dateHeader: AgendaListItem.DateHeader) {
    val label = when (dateHeader.dateTime) {
      LocalDate() -> itemView.context.getString(R.string.today)
      LocalDate().plusDays(1) -> itemView.context.getString(R.string.tomorrow)
      else -> DateTimeFormat.fullDate().print(dateHeader.dateTime)
    }

    itemView.findViewById<TextView>(R.id.date_header).text = label
  }
}