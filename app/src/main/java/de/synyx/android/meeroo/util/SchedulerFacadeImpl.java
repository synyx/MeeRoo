package de.synyx.android.meeroo.util;

import io.reactivex.Scheduler;

import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.schedulers.Schedulers;


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
