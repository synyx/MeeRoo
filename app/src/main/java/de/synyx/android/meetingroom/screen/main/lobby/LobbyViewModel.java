package de.synyx.android.meetingroom.screen.main.lobby;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.domain.MeetingRoom;
import de.synyx.android.meetingroom.util.SchedulerFacade;

import io.reactivex.disposables.Disposable;

import java.util.List;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class LobbyViewModel extends ViewModel {

    private MutableLiveData<List<MeetingRoom>> rooms;

    private LoadVisibleRoomsUseCase loadVisibleRoomsUseCase;
    private SchedulerFacade schedulerFacade;

    private Disposable disposable;

    public LobbyViewModel() {

        loadVisibleRoomsUseCase = new LoadVisibleRoomsUseCase();
        schedulerFacade = Registry.get(SchedulerFacade.class);
    }

    public LiveData<List<MeetingRoom>> getRooms() {

        if (rooms == null) {
            rooms = new MutableLiveData<>();
            loadRooms();
        }

        return rooms;
    }


    private void loadRooms() {

        disposable = loadVisibleRoomsUseCase.execute()
                .observeOn(schedulerFacade.io())
                .subscribeOn(schedulerFacade.mainThread())
                .subscribe(rooms::postValue);
    }


    @Override
    protected void onCleared() {

        super.onCleared();
        disposable.dispose();
    }


    public void tick() {

        if (disposable != null) {
            disposable.dispose();
        }

        loadRooms();
    }
}
