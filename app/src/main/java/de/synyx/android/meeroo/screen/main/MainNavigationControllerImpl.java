package de.synyx.android.meeroo.screen.main;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.screen.main.agenda.AgendaFragment;
import de.synyx.android.meeroo.screen.main.lobby.LobbyFragment;
import de.synyx.android.meeroo.screen.main.status.StatusFragment;
import de.synyx.android.meeroo.screen.settings.SettingsActivity;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
class MainNavigationControllerImpl implements MainNavigationController {

    static final String SELECTED_FRAGMENT_STATUS = "selected_status";
    static final String SELECTED_FRAGMENT_AGENDA = "selected_agenda";
    static final String SELECTED_FRAGMENT_LOBBY = "selected_lobby";

    private String selectedFragment;
    private Context context;
    private NavigationCountDownTimer timer;
    private boolean running;
    private boolean autoNavigationEnabled;

    MainNavigationControllerImpl(Context context, String initialFragment, boolean autoNavigationEnabled,
        int autoNavigationTime) {

        this.context = context;
        this.autoNavigationEnabled = autoNavigationEnabled;

        timer = new NavigationCountDownTimer(toMillis(autoNavigationTime), this::navigateToStatus);

        if (SELECTED_FRAGMENT_STATUS.equals(initialFragment)) {
            navigateToStatus();
        } else if (SELECTED_FRAGMENT_AGENDA.equals(initialFragment)) {
            navigateToAgenda();
        } else {
            navigateToLobby();
        }
    }

    @Override
    public void navigateToLobby() {

        selectedFragment = SELECTED_FRAGMENT_LOBBY;

        replaceFragment(LobbyFragment.newInstance());

        startTimer();
    }


    @Override
    public void navigateToAgenda() {

        selectedFragment = SELECTED_FRAGMENT_AGENDA;

        replaceFragment(AgendaFragment.newInstance());

        startTimer();
    }


    private void startTimer() {

        if (!autoNavigationEnabled) {
            return;
        }

        if (running) {
            timer.cancel();
        }

        timer.start();
        running = true;
    }


    @Override
    public void navigateToStatus() {

        selectedFragment = SELECTED_FRAGMENT_STATUS;

        replaceFragment(StatusFragment.newInstance());

        stopTimer();
    }


    @Override
    public void openSettings() {

        context.startActivity(new Intent(context, SettingsActivity.class));
    }


    @Override
    public void start() {

        startTimer();
    }


    @Override
    public void stop() {

        stopTimer();
    }


    private void stopTimer() {

        running = false;
        timer.cancel();
    }


    private void replaceFragment(Fragment fragment) {

        ((AppCompatActivity) context).getSupportFragmentManager() //
        .beginTransaction() //
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) //
        .replace(R.id.content_main, fragment) //
        .commit();
    }


    @Override
    public String getSelectedFragment() {

        return selectedFragment;
    }


    @Override
    public void onAutoNavigationEnabledChanged(boolean autoNavigationEnabled) {

        this.autoNavigationEnabled = autoNavigationEnabled;
    }


    @Override
    public void onAutonavigationTimeChanged(int autoNavigationTime) {

        stopTimer();
        timer = new NavigationCountDownTimer(toMillis(autoNavigationTime), this::navigateToStatus);
    }


    private int toMillis(int seconds) {

        return seconds * 1000;
    }
}
