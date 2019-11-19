package de.synyx.android.meeroo.screen.login.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import de.synyx.android.meeroo.R;


/**
 * @author  Julian Heetel - heetel@synyx.de
 */
public class MVVMLoginActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "mvvm-login-fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            fragment = new MVVMLoginFragment();
            manager.beginTransaction().replace(R.id.content_main, fragment, FRAGMENT_TAG).commit();
        }
    }
}
