package de.synyx.android.meeroo.util;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class SchedulerFacadeImpl implements SchedulerFacade {

    @Override
    public Scheduler io() {

        return Schedulers.io();
    }


    @Override
    public Scheduler mainThread() {

        return AndroidSchedulers.mainThread();
    }
}
