package de.synyx.android.meeroo.screen.login;

import androidx.annotation.NonNull;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.util.proxy.PermissionManager;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final PreferencesService preferencesService;
    private final PermissionManager permissionManager;
    private final CalendarModeService calendarModeService;

    public LoginViewModelFactory(PreferencesService preferencesService, PermissionManager permissionManager,
        CalendarModeService calendarModeService) {

        this.preferencesService = preferencesService;
        this.permissionManager = permissionManager;
        this.calendarModeService = calendarModeService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return modelClass.cast(new LoginViewModel(preferencesService, permissionManager, calendarModeService));
    }
}
