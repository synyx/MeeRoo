package de.synyx.android.meeroo.screen.login.mvvm;

import android.accounts.AccountManager;

import android.content.Intent;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.synyx.android.meeroo.domain.CalendarMode;

import java.util.List;

import static de.synyx.android.meeroo.screen.login.mvvm.MVVMLoginFragment.PERMISSION_REQUEST_CODE;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class MVVMLoginViewModel extends ViewModel {

    private final MVVMLoginConfig config;
    private final MutableLiveData<LoginStep> loginStep;

    public MVVMLoginViewModel(MVVMLoginConfig config) {

        this.config = config;
        loginStep = new MutableLiveData<>();

        // start with permissions
        loginStep.postValue(LoginStep.PERMISSIONS);
    }

    void askForPermissions(Fragment fragment) {

        List<String> neededPermissions = config.getPermissionManager().getNeededPermissions();

        if (neededPermissions.isEmpty()) {
            loginStep.postValue(LoginStep.ACCOUNT);
        } else {
            String[] permissions = new String[neededPermissions.size()];
            config.getPermissionManager()
                .requestPermission(fragment, neededPermissions.toArray(permissions), PERMISSION_REQUEST_CODE);
        }
    }


    void saveAccount(String accountName) {

        config.getPreferencesService().saveLoginAccountAndType(accountName, accountName, accountName);
        loginStep.postValue(LoginStep.MODE);
    }


    MutableLiveData<LoginStep> getLoginStep() {

        return loginStep;
    }


    void stepAccount(Fragment fragment) {

        if (!config.getPreferencesService().isLoggedIn()) {
            Intent accountIntent = AccountManager.newChooseAccountIntent(null, null, null, null, null, null, null);
            fragment.startActivityForResult(accountIntent, MVVMLoginFragment.REQUEST_ACCOUNT);
        } else {
            loginStep.postValue(LoginStep.MODE);
        }
    }


    void stepMode() {

        if (!TextUtils.isEmpty(config.getPreferencesService().getSelectedCalenderMode())) {
            loginStep.postValue(LoginStep.FINISHED);
        }
    }


    void saveCalendarMode(CalendarMode mode) {

        String modeString = config.getCalendarModeService().getStringCalenderMode(mode);
        config.getPreferencesService().saveCalendarMode(modeString);
        loginStep.postValue(LoginStep.FINISHED);
    }
}
