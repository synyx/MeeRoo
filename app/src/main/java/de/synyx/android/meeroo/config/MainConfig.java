package de.synyx.android.meeroo.config;

import android.app.Application;

import android.content.Context;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.business.account.AccountService;
import de.synyx.android.meeroo.business.account.AccountServiceImpl;
import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.business.calendar.CalendarModeServiceImpl;
import de.synyx.android.meeroo.business.calendar.RoomCalendarRepository;
import de.synyx.android.meeroo.business.event.AttendeeRepository;
import de.synyx.android.meeroo.business.event.EventRepository;
import de.synyx.android.meeroo.data.AttendeeAdapterImpl;
import de.synyx.android.meeroo.data.AttendeeRepositoryImpl;
import de.synyx.android.meeroo.data.CalendarAdapter;
import de.synyx.android.meeroo.data.CalendarAdapterImpl;
import de.synyx.android.meeroo.data.EventAdapterImpl;
import de.synyx.android.meeroo.data.EventRepositoryImpl;
import de.synyx.android.meeroo.data.RoomCalendarRepositoryImpl;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.preferences.PreferencesServiceImpl;
import de.synyx.android.meeroo.util.SchedulerFacade;
import de.synyx.android.meeroo.util.SchedulerFacadeImpl;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class MainConfig {

    private static final String PREFS_NAME = "prefs";

    private MainConfig() {

        // hide
    }

    public static void init(Context context) {

        SharedPreferences legacyPrefs = context.getSharedPreferences(PREFS_NAME, Application.MODE_PRIVATE);
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        PreferencesServiceImpl preferencesService = new PreferencesServiceImpl(legacyPrefs, defaultPreferences);
        Registry.put(PreferencesService.class, preferencesService);

        CalendarModeServiceImpl calendarModeService = new CalendarModeServiceImpl(preferencesService,
                context.getString(R.string.calendarMode), context.getString(R.string.resourcesMode));
        AccountServiceImpl accountService = new AccountServiceImpl(preferencesService, context);

        Registry.put(CalendarModeService.class, calendarModeService);
        Registry.put(AccountService.class, accountService);
        Registry.put(CalendarAdapter.class,
            new CalendarAdapterImpl(preferencesService, context.getContentResolver(), calendarModeService,
                accountService));

        AttendeeAdapterImpl attendeeAdapter = new AttendeeAdapterImpl(context.getContentResolver());
        EventAdapterImpl eventAdapter = new EventAdapterImpl(context.getContentResolver());
        Registry.put(AttendeeRepository.class, new AttendeeRepositoryImpl(attendeeAdapter));
        Registry.put(EventRepository.class, new EventRepositoryImpl(eventAdapter, attendeeAdapter));

        Registry.put(SchedulerFacade.class, new SchedulerFacadeImpl());
        Registry.put(RoomCalendarRepository.class,
            new RoomCalendarRepositoryImpl(Registry.get(CalendarAdapter.class)));
    }
}
