package de.synyx.android.meeroo.screen.main.agenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.domain.Reservation;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class ReservationsRecyclerAdapter extends RecyclerView.Adapter<ReservationViewHolder> {

    private List<Reservation> reservations = new ArrayList<>();

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_reservation, parent, false);

        return new ReservationViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder reservationViewHolder, int index) {

        Reservation reservation = reservations.get(index);
        reservationViewHolder.bind(reservation);
    }


    @Override
    public int getItemCount() {

        return reservations.size();
    }


    public void updateReservations(List<Reservation> newReservations) {

        reservations.clear();
        reservations.addAll(newReservations);

        notifyDataSetChanged();
    }
}
