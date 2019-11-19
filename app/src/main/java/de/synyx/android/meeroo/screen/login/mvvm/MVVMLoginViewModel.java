package de.synyx.android.meeroo.screen.login.mvvm;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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


    public MutableLiveData<LoginStep> getLoginStep() {

        return loginStep;
    }
}
