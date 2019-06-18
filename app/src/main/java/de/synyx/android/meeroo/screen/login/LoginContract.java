package de.synyx.android.meeroo.screen.login;

import de.synyx.android.meeroo.mvp.BasePresenter;
import de.synyx.android.meeroo.mvp.BaseView;

import java.util.List;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public interface LoginContract {

    interface LoginView extends BaseView {

        void askForPermissions(List<String> neededPermissions);


        void showAccountSelection(String[] accounts);


        void showCalendarModeSelection(String[] calendarModes);


        void showErrorDialog();


        void showProgress();
    }

    interface LoginPresenter extends BasePresenter {

        void askForPermissionsAgain();


        void onAllPermissionsGranted();


        void onAccountSelected(String account);


        void onCalendarModeSelected(String calendarMode);


        void onErrorDialogCloseButtonClicked();
    }
}
