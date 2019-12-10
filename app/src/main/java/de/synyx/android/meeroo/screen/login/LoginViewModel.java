package de.synyx.android.meeroo.screen.login;

import android.accounts.AccountManager;

import android.content.Intent;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.domain.CalendarMode;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.util.proxy.PermissionManager;

import java.util.List;

import static de.synyx.android.meeroo.screen.login.LoginFragment.PERMISSION_REQUEST_CODE;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginViewModel extends ViewModel {

    private final PreferencesService preferencesService;
    private final PermissionManager permissionManager;
    private final CalendarModeService calendarModeService;
    private final MutableLiveData<LoginStep> loginStep;

    public LoginViewModel(PreferencesService preferencesService, PermissionManager permissionManager,
        CalendarModeService calendarModeService) {

        this.preferencesService = preferencesService;
        this.permissionManager = permissionManager;
        this.calendarModeService = calendarModeService;

        loginStep = new MutableLiveData<>();

        // start with permissions
        loginStep.postValue(LoginStep.PERMISSIONS);
    }

    void askForPermissions(Fragment fragment) {

        List<String> neededPermissions = permissionManager.getNeededPermissions();

        if (neededPermissions.isEmpty()) {
            loginStep.postValue(LoginStep.ACCOUNT);
        } else {
            String[] permissions = new String[neededPermissions.size()];
            permissionManager.requestPermission(fragment, neededPermissions.toArray(permissions),
                PERMISSION_REQUEST_CODE);
        }
    }


    void saveAccount(String accountName) {

        preferencesService.saveLoginAccountAndType(accountName, accountName, accountName);
        loginStep.postValue(LoginStep.MODE);
    }


    MutableLiveData<LoginStep> getLoginStep() {

        return loginStep;
    }


    void stepAccount(Fragment fragment) {

        if (!preferencesService.isLoggedIn()) {
            Intent accountIntent = AccountManager.newChooseAccountIntent(null, null, null, null, null, null, null);
            fragment.startActivityForResult(accountIntent, LoginFragment.REQUEST_ACCOUNT);
        } else {
            loginStep.postValue(LoginStep.MODE);
        }
    }


    void stepMode() {

        if (!TextUtils.isEmpty(preferencesService.getSelectedCalenderMode())) {
            loginStep.postValue(LoginStep.FINISHED);
        }
    }


    void saveCalendarMode(CalendarMode mode) {

        String modeString = calendarModeService.getStringCalenderMode(mode);
        preferencesService.saveCalendarMode(modeString);
        loginStep.postValue(LoginStep.FINISHED);
    }
}
