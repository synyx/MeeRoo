package de.synyx.android.meeroo.screen.reservation;

import android.arch.lifecycle.ViewModelProviders;

import android.databinding.DataBindingUtil;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.databinding.FragmentReservationBinding;
import de.synyx.android.meeroo.domain.BookingResult;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.screen.main.MainActivity;
import de.synyx.android.meeroo.screen.main.status.MeetingRoomViewModel;


public class ReservationFragment extends Fragment {

    private long calendarId = 0;
    private TextView txtDate;
    private TextView txtStart;
    private TextView txtEnd;
    private Button btnSubmit;
    private ReservationViewModel reservationViewModel;
    private MeetingRoomViewModel meetingRoomViewModel;
    private TextView txtHeading;

    public static ReservationFragment getInstance() {

        return new ReservationFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        FragmentReservationBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation,
                container, false);

        meetingRoomViewModel = ViewModelProviders.of(getActivity()).get(MeetingRoomViewModel.class);
        reservationViewModel = ViewModelProviders.of(getActivity()).get(ReservationViewModel.class);
        dataBinding.setViewModel(reservationViewModel);

        return dataBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setupDateField(view);
        setupStartTimeField(view);
        setupEndTimeField(view);
        setupSubmitButton(view);
        txtHeading = view.findViewById(R.id.reservation_heading);

        meetingRoomViewModel.getRoom().observe(getActivity(), this::updateMeetingRoom);
    }


    private void updateMeetingRoom(MeetingRoom room) {

        calendarId = room.getCalendarId();
    }


    private void setupSubmitButton(View view) {

        btnSubmit = view.findViewById(R.id.event_submit);
        btnSubmit.setOnClickListener(this::saveReservation);
    }


    private void saveReservation(View view) {

        BookingResult bookingResult = reservationViewModel.saveReservation(calendarId);

        if (bookingResult.hasError()) {
            Snackbar.make(view, bookingResult.errorMessage, Snackbar.LENGTH_INDEFINITE);

            return;
        }

        ((MainActivity) getActivity()).openStatusFragment();
    }


    private void setupEndTimeField(View view) {

        txtEnd = view.findViewById(R.id.event_end);
        txtEnd.setOnClickListener(ignored -> showEndTimePicker());
        txtEnd.setOnFocusChangeListener((ignored, hasFocus) -> {
            if (hasFocus) {
                showEndTimePicker();
            }
        });
        reservationViewModel.getEventEndTime().observe(this, time -> txtEnd.setText(time.toString("HH:mm")));
    }


    private void setupStartTimeField(View view) {

        txtStart = view.findViewById(R.id.event_start);
        txtStart.setOnClickListener(ignored -> showStartTimePicker());
        txtStart.setOnFocusChangeListener((ignored, hasFocus) -> {
            if (hasFocus) {
                showStartTimePicker();
            }
        });
        reservationViewModel.getEventStartTime().observe(this, time -> txtStart.setText(time.toString("HH:mm")));
    }


    private void setupDateField(View view) {

        txtDate = view.findViewById(R.id.event_date);
        txtDate.setOnClickListener(ignored -> showDatePicker());
        txtDate.setOnFocusChangeListener((ignored, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });
        reservationViewModel.getEventDate().observe(this, date -> txtDate.setText(date.toString("dd.MM.yyyy")));
    }


    private void showDatePicker() {

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.addOnDateSelectionListener(reservationViewModel::changeEventDate);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "SelectDate");
    }


    private void showStartTimePicker() {

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.addOnTimeSelectionListener(reservationViewModel::changeEventStartTime);
        timePickerFragment.show(getActivity().getSupportFragmentManager(), "SelectStartTime");
    }


    private void showEndTimePicker() {

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.addOnTimeSelectionListener(reservationViewModel::changeEventEndTime);
        timePickerFragment.show(getActivity().getSupportFragmentManager(), "SelectEndTime");
    }
}
