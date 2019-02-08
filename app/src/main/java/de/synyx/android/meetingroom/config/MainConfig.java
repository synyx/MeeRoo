package de.synyx.android.meetingroom.config;

import de.synyx.android.meetingroom.business.calendar.RoomCalendarRepository;
import de.synyx.android.meetingroom.business.event.AttendeeRepository;
import de.synyx.android.meetingroom.business.event.EventRepository;
import de.synyx.android.meetingroom.data.AttendeeAdapter;
import de.synyx.android.meetingroom.data.AttendeeAdapterImpl;
import de.synyx.android.meetingroom.data.AttendeeRepositoryImpl;
import de.synyx.android.meetingroom.data.EventAdapter;
import de.synyx.android.meetingroom.data.EventAdapterImpl;
import de.synyx.android.meetingroom.data.EventRepositoryImpl;
import de.synyx.android.meetingroom.data.RoomCalendarRepositoryImpl;
import de.synyx.android.meetingroom.util.SchedulerFacade;
import de.synyx.android.meetingroom.util.SchedulerFacadeImpl;


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
