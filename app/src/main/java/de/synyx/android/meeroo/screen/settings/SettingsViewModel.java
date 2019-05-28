package de.synyx.android.meeroo.screen.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.MeetingRoom;
import de.synyx.android.meeroo.screen.main.lobby.LoadVisibleRoomsUseCase;
import de.synyx.android.meeroo.util.SchedulerFacade;

import io.reactivex.disposables.Disposable;

import java.util.List;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class SettingsViewModel extends ViewModel {

    private MutableLiveData<List<MeetingRoom>> rooms;

    private LoadVisibleRoomsUseCase loadRoomsUseCase;
    private SchedulerFacade schedulerFacade;
    private Disposable disposable;

    public SettingsViewModel() {

        loadRoomsUseCase = Registry.get(LoadVisibleRoomsUseCase.class);
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

        disposable = loadRoomsUseCase.execute()
                .observeOn(schedulerFacade.io())
                .subscribeOn(schedulerFacade.mainThread())
                .subscribe(rooms::postValue);
    }


    @Override
    protected void onCleared() {

        super.onCleared();
        disposable.dispose();
    }
}
