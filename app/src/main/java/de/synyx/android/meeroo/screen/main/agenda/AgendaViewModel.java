package de.synyx.android.meeroo.screen.main.agenda;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.screen.main.status.LoadRoomUseCase;
import de.synyx.android.meeroo.util.SchedulerFacade;
import io.reactivex.rxjava3.disposables.Disposable;


public class AgendaViewModel extends ViewModel {

    private MutableLiveData<MeetingRoom> meetingRoom;
    private SchedulerFacade schedulerFacade;
    private LoadRoomUseCase loadRoomUseCase;
    private Disposable disposable;

    public AgendaViewModel() {

        schedulerFacade = Registry.get(SchedulerFacade.class);
        loadRoomUseCase = new LoadRoomUseCase();
    }

    public LiveData<MeetingRoom> getAgenda(long calendarId) {

        if (meetingRoom == null) {
            meetingRoom = new MutableLiveData<>();
            loadAgenda(calendarId);
        }

        return meetingRoom;
    }


    private void loadAgenda(long calendarId) {

        disposable =
            loadRoomUseCase.execute(calendarId) //
            .observeOn(schedulerFacade.io()) //
            .subscribeOn(schedulerFacade.mainThread()) //
            .subscribe(meetingRoom::postValue);
    }


    @Override
    protected void onCleared() {

        super.onCleared();
        disposable.dispose();
    }
}
