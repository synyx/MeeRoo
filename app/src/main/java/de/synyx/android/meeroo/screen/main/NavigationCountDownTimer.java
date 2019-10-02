package de.synyx.android.meeroo.screen.main;

import android.os.CountDownTimer;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class NavigationCountDownTimer extends CountDownTimer {

    private final Listener listener;

    NavigationCountDownTimer(int millis, Listener listener) {

        // user same value for time and interval as interval is not needed
        super(millis, millis);
        this.listener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {

        // not needed
    }


    @Override
    public void onFinish() {

        listener.onTimerFinished();
    }

    interface Listener {

        void onTimerFinished();
    }
}
