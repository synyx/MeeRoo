package de.synyx.android.meeroo.util;

import io.reactivex.Scheduler;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface SchedulerFacade {

    Scheduler io();


    Scheduler mainThread();
}
