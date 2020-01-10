package de.synyx.android.meeroo.screen.main.status;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.domain.BookingResult;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.domain.Reservation;
import de.synyx.android.meeroo.domain.RoomAvailability;
import de.synyx.android.meeroo.screen.main.MainActivity;
import de.synyx.android.meeroo.util.DateFormatter;
import de.synyx.android.meeroo.util.livedata.SingleEvent;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;
import static de.synyx.android.meeroo.domain.RoomAvailability.AVAILABLE;

import static org.joda.time.Duration.standardMinutes;


public class StatusFragment extends Fragment {

    private MeetingRoomViewModel viewModel;
    private TextView tvStatus;
    private TextView tvTimeInfo;
    private TextView tvNextEventName;
    private TextView tvNextEventTime;
    private TextView tvSecondNextEventName;
    private TextView tvSecondNextEventTime;
    private Button btnBook15;
    private Button btnBook30;
    private Button btnBook60;
    private Button btnEndNow;
    private ViewGroup fragmentContainer;

    public StatusFragment() {

        // Required empty public constructor
    }

    public static StatusFragment newInstance() {

        return new StatusFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_status, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(MeetingRoomViewModel.class);
        viewModel.getRoom().observe(this, this::updateStatus);
        viewModel.getBookingResult().observe(this, this::showSnackbarOnError);

        fragmentContainer = view.findViewById(R.id.status_fragment_container);

        tvStatus = view.findViewById(R.id.status_text);
        tvTimeInfo = view.findViewById(R.id.time_info);
        tvNextEventName = view.findViewById(R.id.next_event);
        tvNextEventTime = view.findViewById(R.id.next_event_time);
        tvSecondNextEventName = view.findViewById(R.id.second_next_event);
        tvSecondNextEventTime = view.findViewById(R.id.second_next_event_time);

        btnBook15 = view.findViewById(R.id.book_15);
        btnBook30 = view.findViewById(R.id.book_30);
        btnBook60 = view.findViewById(R.id.book_60);
        btnEndNow = view.findViewById(R.id.end_now);
    }


    private void showSnackbarOnError(SingleEvent<BookingResult> bookingResultEvent) {

        BookingResult bookingResult = bookingResultEvent.getContentIfNotHandled();

        if (bookingResult != null && bookingResult.hasError()) {
            Snackbar.make(getView(), bookingResult.errorMessage, LENGTH_LONG).show();
        }
    }


    void updateStatus(MeetingRoom meetingRoom) {

        MainActivity activity = (MainActivity) getActivity();
        activity.setTitle(meetingRoom.getName());

        RoomAvailability roomAvailability = meetingRoom.getAvailability();
        fragmentContainer.setBackgroundColor(getActivity().getColor(roomAvailability.getColorRes()));

        setupBookNowButton(roomAvailability, btnBook15, standardMinutes(15));
        setupBookNowButton(roomAvailability, btnBook30, standardMinutes(30));
        setupBookNowButton(roomAvailability, btnBook60, standardMinutes(60));
        setupEndNowButton(roomAvailability);

        tvStatus.setText(roomAvailability.getStringRes());
        tvTimeInfo.setText(getTextForEventDuration(meetingRoom.getAvailabilityTime(), roomAvailability));
        setNextEvent(meetingRoom);
        setSecondNextEvent(meetingRoom);
        setCurrentMeeting(meetingRoom);
    }


    private void setCurrentMeeting(MeetingRoom meetingRoom) {

        String currentMeetingText = getCurrentMeetingText(meetingRoom);

        if (currentMeetingText != null) {
            tvStatus.setText(currentMeetingText);
        }
    }


    private String getTextForEventDuration(Duration duration, RoomAvailability availability) {

        if (duration == null) {
            return getString(R.string.status_availability_time_placeholder);
        }

        if (availability == RoomAvailability.UNAVAILABLE) {
            return getString(R.string.status_availability_time_unavailable, formatDuration(duration));
        }

        return getString(R.string.status_availability_time_available, formatDuration(duration));
    }


    private String formatDuration(Duration duration) {

        return DateFormatter.periodFormatter(getContext()).print(duration.toPeriod());
    }


    private void setupBookNowButton(RoomAvailability roomAvailability, Button button, Duration minutes) {

        button.setTextColor(getActivity().getColor(roomAvailability.getColorRes()));
        button.setOnClickListener(view ->
                new BookNowDialogFragment().addDuration(minutes).show(getFragmentManager(), "BookNowDialog"));
        button.setVisibility(roomAvailability == AVAILABLE ? VISIBLE : GONE);
    }


    private void setupEndNowButton(RoomAvailability roomAvailability) {

        btnEndNow.setOnClickListener(view -> new EndNowDialogFragment().show(getFragmentManager(), "EndNowDialog"));
        btnEndNow.setVisibility(roomAvailability == RoomAvailability.UNAVAILABLE ? VISIBLE : GONE);
    }


    private void setNextEvent(MeetingRoom meetingRoom) {

        Reservation upcomingReservation = meetingRoom.getUpcomingReservation();

        tvNextEventTime.setVisibility(upcomingReservation == null ? GONE : VISIBLE);
        tvNextEventTime.setText(getEventBegin(upcomingReservation));
        tvNextEventName.setText(getEventName(upcomingReservation));
    }


    private void setSecondNextEvent(MeetingRoom meetingRoom) {

        Reservation upcomingReservation = meetingRoom.getSecondUpcomingReserveration();

        tvSecondNextEventTime.setVisibility(upcomingReservation == null ? GONE : VISIBLE);
        tvSecondNextEventTime.setText(getEventBegin(upcomingReservation));
        tvSecondNextEventName.setVisibility(upcomingReservation == null ? INVISIBLE : VISIBLE);
        tvSecondNextEventName.setText(getEventName(upcomingReservation));
    }


    @NonNull
    private String getEventName(Reservation reservation) {

        if (reservation == null) {
            return getString(R.string.status_next_event_placeholder);
        }

        return reservation.getTitle();
    }


    @NonNull
    private String getEventBegin(Reservation reservation) {

        if (reservation == null) {
            return "";
        }

        return formatAsTime(reservation.getBegin());
    }


    private String formatAsTime(DateTime time) {

        return time.toString("HH:mm");
    }


    private String getCurrentMeetingText(MeetingRoom meetingRoom) {

        Reservation currentMeeting = meetingRoom.getCurrentMeeting();

        return currentMeeting != null ? currentMeeting.getTitle() : null;
    }
}
