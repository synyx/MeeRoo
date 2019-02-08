package de.synyx.android.meetingroom.screen.reservation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import android.databinding.ObservableField;

import de.synyx.android.meetingroom.domain.BookingResult;
import de.synyx.android.meetingroom.domain.MeetingRoom;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class ReservationViewModel extends ViewModel {

    public final ObservableField<String> eventName = new ObservableField<>();
    private MutableLiveData<MeetingRoom> meetingRoom;
    private MutableLiveData<LocalDate> eventDate;
    private MutableLiveData<LocalTime> eventStartTime;
    private MutableLiveData<LocalTime> eventEndTime;

    public LiveData<MeetingRoom> getMeetingRoom() {

        if (meetingRoom == null) {
            meetingRoom = new MutableLiveData<>();
        }

        return meetingRoom;
    }


    public void setMeetingRoom(MeetingRoom room) {

        meetingRoom.postValue(room);
    }


    public LiveData<LocalDate> getEventDate() {

        if (eventDate == null) {
            eventDate = new MutableLiveData<>();
            eventDate.postValue(LocalDate.now());
        }

        return eventDate;
    }


    public void changeEventDate(LocalDate date) {

        eventDate.postValue(date);
    }


    public LiveData<LocalTime> getEventStartTime() {

        if (eventStartTime == null) {
            eventStartTime = new MutableLiveData<>();

            eventStartTime.postValue(atFullHour(LocalTime.now().plusHours(1)));
        }

        return eventStartTime;
    }


    public void changeEventStartTime(LocalTime time) {

        eventStartTime.postValue(time);
    }


    public LiveData<LocalTime> getEventEndTime() {

        if (eventEndTime == null) {
            eventEndTime = new MutableLiveData<>();
            eventEndTime.postValue(atFullHour(LocalTime.now().plusHours(2)));
        }

        return eventEndTime;
    }


    public void changeEventEndTime(LocalTime time) {

        eventEndTime.postValue(time);
    }


    public BookingResult saveReservation(long calendarId) {

        return
            new AddReservationUseCase().execute(calendarId, eventName.get(), eventDate.getValue(),
                eventStartTime.getValue(), eventEndTime.getValue());
    }


    public static LocalTime atFullHour(LocalTime time) {

        return time.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
    }
}
