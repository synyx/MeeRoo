package de.synyx.android.meeroo.config;

import de.synyx.android.meeroo.business.calendar.RoomCalendarRepository;
import de.synyx.android.meeroo.business.event.AttendeeRepository;
import de.synyx.android.meeroo.business.event.EventRepository;
import de.synyx.android.meeroo.data.AttendeeAdapter;
import de.synyx.android.meeroo.data.AttendeeAdapterImpl;
import de.synyx.android.meeroo.data.AttendeeRepositoryImpl;
import de.synyx.android.meeroo.data.EventAdapter;
import de.synyx.android.meeroo.data.EventAdapterImpl;
import de.synyx.android.meeroo.data.EventRepositoryImpl;
import de.synyx.android.meeroo.data.RoomCalendarRepositoryImpl;
import de.synyx.android.meeroo.util.SchedulerFacade;
import de.synyx.android.meeroo.util.SchedulerFacadeImpl;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class MainConfig {

    private MainConfig() {

        // hide
    }

    public static void init() {

        Registry.put(AttendeeAdapter.class, new AttendeeAdapterImpl());
        Registry.put(EventAdapter.class, new EventAdapterImpl());
        Registry.put(AttendeeRepository.class, new AttendeeRepositoryImpl());
        Registry.put(EventRepository.class, new EventRepositoryImpl());
        Registry.put(SchedulerFacade.class, new SchedulerFacadeImpl());
        Registry.put(RoomCalendarRepository.class, new RoomCalendarRepositoryImpl());
    }
}
