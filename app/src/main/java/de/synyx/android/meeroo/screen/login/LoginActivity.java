package de.synyx.android.meeroo.screen.login;

import android.accounts.AccountManager;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.domain.CalendarMode;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.screen.main.MainActivity;


public class LoginActivity extends AppCompatActivity implements LoginListener {

    private static final String FRAGMENT_TAG = "login-fragment";
    private static final int REQUEST_ACCOUNT = 139;
    private PreferencesService preferencesService;
    private LoginContract.LoginPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            fragment = new LoginFragment();
            fragmentManager.beginTransaction().replace(R.id.relativeLayout1, fragment).commit();
        }

        LoginPresenterFactory presenterFactory = Registry.get(LoginPresenterFactory.class);
        presenter = presenterFactory.createPresenter((LoginContract.LoginView) fragment, this);
        ((LoginFragment) fragment).setPresenter(presenter);

        preferencesService = Registry.get(PreferencesService.class);

        if (preferencesService.isLoggedIn()) {
            startMainActivty();
        } else {
            Intent accountIntent = AccountManager.newChooseAccountIntent(null, null, null, null, null, null, null);
            startActivityForResult(accountIntent, REQUEST_ACCOUNT);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        finish();
    }


    @Override
    public void onErrorCloseButtonClick() {

        finish();
    }


    @Override
    public void onCalenderModeClick(CalendarMode calendarMode) {

        startMainActivty();
    }


    private void startMainActivty() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
