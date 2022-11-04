package de.synyx.android.meeroo.screen.main.agenda

import androidx.recyclerview.widget.RecyclerView
import de.synyx.android.meeroo.screen.main.agenda.ReservationViewHolder
import de.synyx.android.meeroo.domain.Reservation
import android.view.ViewGroup
import android.view.LayoutInflater
import de.synyx.android.meeroo.R
import java.util.ArrayList

/**
 * @author  Max Dobler - dobler@synyx.de
 */
class ReservationsRecyclerAdapter : RecyclerView.Adapter<ReservationViewHolder>() {
  private val reservations: MutableList<Reservation> = ArrayList()
  override fun onCreateViewHolder(parent: ViewGroup, i: Int): ReservationViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_agenda_reservation, parent, false)
    return ReservationViewHolder(view)
  }

  override fun onBindViewHolder(reservationViewHolder: ReservationViewHolder, index: Int) {
    val reservation = reservations[index]
    reservationViewHolder.bind(reservation)
  }

  override fun getItemCount(): Int {
    return reservations.size
  }

  fun updateReservations(newReservations: List<Reservation>?) {
    reservations.clear()
    reservations.addAll(newReservations!!)
    notifyDataSetChanged()
  }
}