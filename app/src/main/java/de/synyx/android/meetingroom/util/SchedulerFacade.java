package de.synyx.android.meetingroom.util;

import io.reactivex.Scheduler;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public interface SchedulerFacade {

    Scheduler io();


    Scheduler mainThread();
}
