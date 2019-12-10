package de.synyx.android.meeroo.screen.login.mvvm;

import android.content.Intent;

import android.os.Bundle;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.screen.main.MainActivity;
import de.synyx.android.meeroo.util.proxy.PermissionManager;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class MVVMLoginActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "mvvm-login-fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (isSetupCompleted()) {
            startMainActivity();

            return;
        }

        setContentView(R.layout.activity_login);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            fragment = new MVVMLoginFragment();
            manager.beginTransaction().replace(R.id.content_main, fragment, FRAGMENT_TAG).commit();
        }
    }


    private boolean isSetupCompleted() {

        PreferencesService preferencesService = Registry.get(PreferencesService.class);
        PermissionManager permissionManager = Registry.get(PermissionManager.class);

        return preferencesService.isLoggedIn() && permissionManager.getNeededPermissions().isEmpty()
            && !TextUtils.isEmpty(preferencesService.getSelectedCalenderMode());
    }


    void startMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
