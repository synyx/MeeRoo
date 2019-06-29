package de.synyx.android.meeroo.screen;

import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public abstract class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onResume() {

        super.onResume();
        enableFullscreen();
        keepScreenOn();
    }


    private void keepScreenOn() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    protected void enableFullscreen() {

        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY //
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE //
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
