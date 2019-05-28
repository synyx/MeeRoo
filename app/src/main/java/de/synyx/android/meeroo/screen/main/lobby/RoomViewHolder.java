package de.synyx.android.meeroo.screen.main.lobby;

import android.content.Context;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.TextView;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.domain.Reservation;
import de.synyx.android.meeroo.domain.RoomAvailability;
import de.synyx.android.meeroo.util.DateFormatter;

import org.joda.time.Duration;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
class RoomViewHolder extends RecyclerView.ViewHolder {

    private TextView meetingRommName;
    private TextView availabilityTime;
    private TextView currentMeetingTitle;
    private TextView upcomingReservationTitle;

    RoomViewHolder(@NonNull View itemView) {

        super(itemView);

        meetingRommName = itemView.findViewById(R.id.roomName);
        availabilityTime = itemView.findViewById(R.id.roomTime);
        currentMeetingTitle = itemView.findViewById(R.id.current_meeting_title);
        upcomingReservationTitle = itemView.findViewById(R.id.upcoming_reservervation_title);
    }

    void setStatus(RoomAvailability roomAvailability) {

        itemView.setBackgroundResource(roomAvailability.getColorRes());
    }


    public void bind(MeetingRoom meetingRoom) {

        RoomAvailability availability = meetingRoom.getAvailability();
        setStatus(availability);

        meetingRommName.setText(meetingRoom.getName());
        availabilityTime.setText(getTextForAvailabilityTime(meetingRoom.getAvailabilityTime(), availability));
        currentMeetingTitle.setText(getCurrentMeetingText(meetingRoom));
        upcomingReservationTitle.setText(getUpcomingReservationText(meetingRoom));
    }


    private String getTextForAvailabilityTime(Duration duration, RoomAvailability roomAvailability) {

        if (duration == null) {
            return getContext().getString(R.string.lobby_room_availability_time_placeholder);
        }

        if (roomAvailability == RoomAvailability.UNAVAILABLE) {
            return getContext().getString(R.string.lobby_room_availability_time_unavailable, formatDuration(duration));
        }

        return getContext().getString(R.string.lobby_room_availability_time_available, formatDuration(duration));
    }


    private Context getContext() {

        return itemView.getContext();
    }


    private String formatDuration(Duration duration) {

        return DateFormatter.smallPeriodFormatter().print(duration.toPeriod());
    }


    private String getUpcomingReservationText(MeetingRoom meetingRoom) {

        Reservation upcomingReservation = meetingRoom.getUpcomingReservation();

        return upcomingReservation != null //
            ? getContext().getString(R.string.lobby_room_item_next_event, upcomingReservation.getTitle()) //
            : getContext().getString(R.string.lobby_room_item_next_event_placeholder);
    }


    private String getCurrentMeetingText(MeetingRoom meetingRoom) {

        Reservation currentMeeting = meetingRoom.getCurrentMeeting();

        return currentMeeting != null ? currentMeeting.getTitle() : "";
    }
}
