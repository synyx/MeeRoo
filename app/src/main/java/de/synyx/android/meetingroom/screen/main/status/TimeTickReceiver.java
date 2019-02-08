package de.synyx.android.meetingroom.screen.main.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.reactivex.Observable;

import io.reactivex.subjects.PublishSubject;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class TimeTickReceiver extends BroadcastReceiver {

    private PublishSubject<Integer> ticks = PublishSubject.create();

    public Observable<Integer> getTicks() {

        return ticks;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        ticks.onNext(1);
    }
}
