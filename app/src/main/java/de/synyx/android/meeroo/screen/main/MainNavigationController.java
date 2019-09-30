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
class MainNavigationController {

    static final String SELECTED_FRAGMENT_STATUS = "selected_status";
    static final String SELECTED_FRAGMENT_AGENDA = "selected_agenda";
    static final String SELECTED_FRAGMENT_LOBBY = "selected_lobby";

    private String selectedFragment;

    private Context context;

    MainNavigationController(Context context) {

        this.context = context;
    }

    void navigateLobby() {

        selectedFragment = SELECTED_FRAGMENT_LOBBY;

        replaceFragment(LobbyFragment.newInstance());
    }


    void navigateAgenda() {

        selectedFragment = SELECTED_FRAGMENT_AGENDA;

        replaceFragment(AgendaFragment.newInstance());
    }


    void navigateStatus() {

        selectedFragment = SELECTED_FRAGMENT_STATUS;

        replaceFragment(StatusFragment.newInstance());
    }


    void openSettings() {

        context.startActivity(new Intent(context, SettingsActivity.class));
    }


    private void replaceFragment(Fragment fragment) {

        ((AppCompatActivity) context).getSupportFragmentManager() //
        .beginTransaction() //
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) //
        .replace(R.id.content_main, fragment) //
        .commit();
    }


    String getSelectedFragment() {

        return selectedFragment;
    }
}
