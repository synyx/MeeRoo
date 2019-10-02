package de.synyx.android.meeroo.screen.main;

/**
 * @author  Julian Heetel - heetel@synyx.de
 *
 *          <p>This class handles navigation between Lobby-, Agenda- and Status-Fragment. If a default room is selected
 *          and StatusFragment is not displayed, the {@link MainNavigationController} navigates to the
 *          {@link de.synyx.android.meeroo.screen.main.status.StatusFragment} after specified time.</p>
 */
interface MainNavigationController {

    void navigateToLobby();


    void navigateToAgenda();


    void navigateToStatus();


    String getSelectedFragment();


    void openSettings();


    void start();


    void stop();


    void onAutoNavigationEnabledChanged(boolean autoNavigationEnabled);


    void onAutonavigationTimeChanged(int autoNavigationTime);
}
