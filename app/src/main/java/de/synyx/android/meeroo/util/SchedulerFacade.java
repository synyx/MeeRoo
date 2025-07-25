package de.synyx.android.meeroo.util;

import io.reactivex.rxjava3.core.Scheduler;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface SchedulerFacade {

    Scheduler io();


    Scheduler mainThread();
}
