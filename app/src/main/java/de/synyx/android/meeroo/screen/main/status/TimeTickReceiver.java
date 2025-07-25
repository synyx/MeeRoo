package de.synyx.android.meeroo.screen.main.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;


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
