package de.synyx.android.meeroo.screen.main.agenda

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import de.synyx.android.meeroo.R
import de.synyx.android.meeroo.domain.Reservation
import org.joda.time.LocalDate

/**
 * @author  Max Dobler - dobler@synyx.de
 */
class ReservationsRecyclerAdapter : RecyclerView.Adapter<ViewHolder>() {

  private val listItems: MutableList<AgendaListItem> = mutableListOf()

  override fun getItemViewType(position: Int): Int {
    return when (listItems[position]) {
      is AgendaListItem.DateHeader -> 0
      is AgendaListItem.ReservationItem -> 1
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    when (viewType) {
      0 -> DateHeaderViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_agenda_date_header, parent, false)
      )
      1 -> ReservationViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_agenda_reservation, parent, false)
      )
      else -> throw IllegalArgumentException("Can't create view holder for illegal viewType $viewType")
    }

  override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
    when (viewHolder) {
      is DateHeaderViewHolder -> viewHolder.bind(listItems[index] as AgendaListItem.DateHeader)
      is ReservationViewHolder -> viewHolder.bind(listItems[index] as AgendaListItem.ReservationItem)
    }
  }

  override fun getItemCount(): Int {
    return listItems.size
  }

  @SuppressLint("NotifyDataSetChanged")
  fun updateReservations(newReservations: List<Reservation>) {
    listItems.clear()

    newReservations.groupBy { it.begin.toLocalDate() }
      .map { entry ->
        listOf(AgendaListItem.DateHeader(entry.key)) +
            entry.value.map { AgendaListItem.ReservationItem(it) }
      }.flatten()
      .also { listItems.addAll(it) }

    notifyDataSetChanged()
  }
}

sealed class AgendaListItem {
  data class DateHeader(val dateTime: LocalDate) : AgendaListItem()
  data class ReservationItem(val reservation: Reservation) : AgendaListItem()
}