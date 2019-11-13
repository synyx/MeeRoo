package de.synyx.android.meeroo.screen.login;

import de.synyx.android.meeroo.business.account.AccountService;
import de.synyx.android.meeroo.business.calendar.CalendarModeService;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.CalendarMode;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.screen.login.LoginContract.LoginPresenter;
import de.synyx.android.meeroo.screen.login.LoginContract.LoginView;
import de.synyx.android.meeroo.util.proxy.PermissionManager;

import java.util.List;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginPresenterImpl implements LoginPresenter {

    private final LoginListener listener;
    private final PreferencesService preferencesService;
    private final PermissionManager permissionManager;
    private final CalendarModeService calendarModeService;
    private CalendarMode prefCalendarMode;
    private LoginView view;

    public LoginPresenterImpl(LoginView view, LoginListener listener, PreferencesService preferencesService,
        CalendarModeService calendarModeService, PermissionManager permissionManager) {

        this.view = view;
        this.calendarModeService = calendarModeService;
        this.listener = listener;
        this.preferencesService = preferencesService;
        this.prefCalendarMode = calendarModeService.getPrefCalenderMode();
        this.permissionManager = permissionManager;
    }

    @Override
    public void askForPermissionsAgain() {

        // TODO ask for permissions after account selected
        askForPermissions();
    }


    @Override
    public void onAllPermissionsGranted() {

        if (hasFatalError()) {
            view.showErrorDialog();
//        } else if (!preferencesService.isLoggedIn()) {
//            checkAccounts();
//        } else {
//            calenderModeSelection();
        }
    }


    @Override
    public void onAccountSelected(String account) {

//        saveSelectedAccount(account);
        calenderModeSelection();
    }


    @Override
    public void onCalendarModeSelected(String calendarMode) {

        preferencesService.saveCalendarMode(calendarMode);

        listener.onCalenderModeClick(prefCalendarMode);
    }


    @Override
    public void onErrorDialogCloseButtonClicked() {

        listener.onErrorCloseButtonClick();
    }


    @Override
    public void start() {

        view.showProgress();

        askForPermissions();
    }


    @Override
    public void stop() {

        // not needed
    }


    private void askForPermissions() {

        if (!view.isAdded()) {
            return;
        }

        List<String> neededPermissions = permissionManager.getNeededPermissions();

        if (neededPermissions.isEmpty()) {
            onAllPermissionsGranted();
        } else {
            view.askForPermissions(neededPermissions);
        }
    }


    private boolean hasFatalError() {

        return false;
    }


    private void checkAccounts() {

        String[] accountNames = Registry.get(AccountService.class).getAccountNames();

        if (accountNames.length == 1) {
            saveSelectedAccount(accountNames[0]);
        } else {
            view.showAccountSelection(accountNames);
        }
    }


    private void saveSelectedAccount(String account) {

        preferencesService.saveLoginAccountAndType(account, account, account.substring(account.indexOf("@") + 1));
    }


    private void calenderModeSelection() {

        if (prefCalendarMode == CalendarMode.NO_SELECTED_MODE) {
            view.showCalendarModeSelection(calendarModeService.getCalendarModes());
        } else {
            listener.onCalenderModeClick(prefCalendarMode);
        }
    }
}
