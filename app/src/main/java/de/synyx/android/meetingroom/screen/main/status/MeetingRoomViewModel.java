package de.synyx.android.meetingroom.screen.main.status;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.domain.BookingResult;
import de.synyx.android.meetingroom.domain.MeetingRoom;
import de.synyx.android.meetingroom.util.SchedulerFacade;
import de.synyx.android.meetingroom.util.livedata.SingleEvent;

import io.reactivex.disposables.Disposable;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class MeetingRoomViewModel extends ViewModel {

    private MutableLiveData<MeetingRoom> room;
    private MutableLiveData<SingleEvent<BookingResult>> bookingResult;

    private final LoadRoomUseCase loadRoomUseCase;
    private final BookNowUseCase bookNowUseCase;
    private final SchedulerFacade schedulerFacade;
    private final EndNowUseCase endNowUseCase;

    private long calendarId;
    private Disposable disposable;

    public MeetingRoomViewModel() {

        loadRoomUseCase = new LoadRoomUseCase();
        bookNowUseCase = new BookNowUseCase();
        schedulerFacade = Registry.get(SchedulerFacade.class);
        endNowUseCase = new EndNowUseCase();
    }

    public LiveData<MeetingRoom> getRoom() {

        loadRoom();

        return room;
    }


    public LiveData<SingleEvent<BookingResult>> getBookingResult() {

        if (bookingResult == null) {
            bookingResult = new MutableLiveData<>();
        }

        return bookingResult;
    }


    public void setCalendarId(Long calendarId) {

        this.calendarId = calendarId;
    }


    private void loadRoom() {

        if (room == null) {
            room = new MutableLiveData<>();
        }

        disposable = loadRoomUseCase.execute(calendarId)
                .observeOn(schedulerFacade.io())
                .subscribeOn(schedulerFacade.mainThread())
                .subscribe(room::postValue);
    }


    public void tick() {

        if (disposable != null) {
            disposable.dispose();
        }

        loadRoom();
    }


    @Override
    protected void onCleared() {

        super.onCleared();
        disposable.dispose();
    }


    public void bookNow() {

        BookingResult result = bookNowUseCase.execute(calendarId, room.getValue());
        this.bookingResult.postValue(SingleEvent.withContent(result));
        tick();
    }


    public void endNow() {

        endNowUseCase.execute(room.getValue());
        tick();
    }
}
