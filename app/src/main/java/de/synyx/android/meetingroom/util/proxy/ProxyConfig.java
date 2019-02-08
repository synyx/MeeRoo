package de.synyx.android.meetingroom.util.proxy;

import android.content.ContentResolver;
import android.content.Context;

import android.support.annotation.NonNull;

import de.synyx.android.meetingroom.business.account.AccountService;
import de.synyx.android.meetingroom.business.account.AccountServiceImpl;
import de.synyx.android.meetingroom.business.calendar.CalendarModeService;
import de.synyx.android.meetingroom.business.calendar.CalendarModeServiceImpl;
import de.synyx.android.meetingroom.config.Config;
import de.synyx.android.meetingroom.config.Registry;
import de.synyx.android.meetingroom.data.CalendarAdapter;
import de.synyx.android.meetingroom.data.CalendarAdapterImpl;


/**
 * @author  Julia Dasch - dasch@synyx.de
 */
public class ProxyConfig {

    private ProxyConfig() {

        // hide
    }

    public static void init(@NonNull Context context) {

        Registry.put(PermissionManager.class, new PermissionManager(context));
        Registry.put(ContentResolver.class, context.getContentResolver());
        Registry.put(CalendarModeService.class, new CalendarModeServiceImpl(context));
        Registry.put(AccountService.class, new AccountServiceImpl(context));
        Registry.put(CalendarAdapter.class,
            new CalendarAdapterImpl(Config.getInstance(context).getPreferencesService()));
    }
}
