package de.synyx.android.meetingroom.screen.login;

import de.synyx.android.meetingroom.preferences.PreferencesService;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class LoginPresenterFactory {

    public LoginContract.LoginPresenter createPresenter(LoginContract.LoginView view, LoginListener listener,
        PreferencesService preferencesService) {

        return new LoginPresenterImpl(view, listener, preferencesService);
    }
}
