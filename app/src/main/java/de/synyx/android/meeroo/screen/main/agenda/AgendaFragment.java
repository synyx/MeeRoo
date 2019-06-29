package de.synyx.android.meeroo.screen.main.agenda;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.screen.main.MainActivity;
import de.synyx.android.meeroo.screen.main.status.MeetingRoomViewModel;


public class AgendaFragment extends Fragment {

    private MeetingRoomViewModel viewModel;
    private ReservationsRecyclerAdapter reservationsRecyclerAdapter;

    public static AgendaFragment newInstance() {

        return new AgendaFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(MeetingRoomViewModel.class);

        setupReservationsRecyclerView(view);

        loadData();
    }


    private void loadData() {

        viewModel.getRoom() //
        .observe(this,
            meetingRoom -> {
                reservationsRecyclerAdapter.updateReservations(meetingRoom.getReservations());

                String roomName = meetingRoom.getName();
                setHeaderTitle(getString(R.string.agenda_title, roomName));
            });
    }


    private void setupReservationsRecyclerView(@NonNull View view) {

        RecyclerView reservationsRecyclerView = view.findViewById(R.id.agenda_reservations);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reservationsRecyclerAdapter = new ReservationsRecyclerAdapter();
        reservationsRecyclerView.setAdapter(reservationsRecyclerAdapter);
    }


    private void setHeaderTitle(String title) {

        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(title);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
