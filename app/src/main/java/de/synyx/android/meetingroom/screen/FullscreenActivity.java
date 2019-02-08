package de.synyx.android.meetingroom.screen;

import android.support.v7.app.AppCompatActivity;

import android.view.View;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public abstract class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onResume() {

        super.onResume();
        enableFullscreen();
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
