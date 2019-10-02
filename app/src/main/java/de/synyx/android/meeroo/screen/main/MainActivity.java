package de.synyx.android.meeroo.screen.main;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.business.account.AccountService;
import de.synyx.android.meeroo.config.Registry;
import de.synyx.android.meeroo.preferences.PreferencesService;
import de.synyx.android.meeroo.screen.FullscreenActivity;
import de.synyx.android.meeroo.screen.main.lobby.LobbyFragment;
import de.synyx.android.meeroo.screen.main.status.BookNowDialogFragment;
import de.synyx.android.meeroo.screen.main.status.EndNowDialogFragment;
import de.synyx.android.meeroo.screen.main.status.MeetingRoomViewModel;
import de.synyx.android.meeroo.screen.main.status.TimeTickReceiver;

import org.joda.time.Duration;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import static de.synyx.android.meeroo.screen.main.MainNavigationControllerImpl.SELECTED_FRAGMENT_AGENDA;
import static de.synyx.android.meeroo.screen.main.MainNavigationControllerImpl.SELECTED_FRAGMENT_LOBBY;
import static de.synyx.android.meeroo.screen.main.MainNavigationControllerImpl.SELECTED_FRAGMENT_STATUS;


public class MainActivity extends FullscreenActivity implements LobbyFragment.RoomSelectionListener,
    BookNowDialogFragment.BookNowDialogListener, EndNowDialogFragment.EndNowOnDialogListener {

    private static final String KEY_SELECTED_MENU_ITEM = "key_selected_menu_item";

    private MainNavigationController navigationController;
    private TextView headerTitle;
    protected MeetingRoomViewModel roomViewModel;
    private TimeTickReceiver timeTickReceiver;
    private AccountService accountService;
    private PreferencesService preferencesService;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        accountService = Registry.get(AccountService.class);

        preferencesService = Registry.get(PreferencesService.class);

        initViewModel();

        timeTickReceiver = new TimeTickReceiver();

        // noinspection ResultOfMethodCallIgnored
        timeTickReceiver.getTicks().subscribe(ignored -> doEveryMinute());

        setContentView(R.layout.activity_main);

        headerTitle = findViewById(R.id.header_title);

        initFragmentNavigation(savedInstanceState);

        setClock();
    }


    private void initViewModel() {

        roomViewModel = ViewModelProviders.of(this).get(MeetingRoomViewModel.class);

        Long defaultRoom = preferencesService.getCalendarIdOfDefaultRoom();

        // if no default room set, use last selected room
        if (defaultRoom == -1L) {
            defaultRoom = preferencesService.getCalendarIdOfLastSelectedRoom();
        }

        roomViewModel.setCalendarId(defaultRoom);
    }


    private void initFragmentNavigation(Bundle savedInstanceState) {

        String selectedFragment = "";

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SELECTED_MENU_ITEM)) {
            selectedFragment = savedInstanceState.getString(KEY_SELECTED_MENU_ITEM);
        }

        boolean autoNavigationEnabled = preferencesService.isAutoNavigationEnabled();
        int autoNavigationTime = preferencesService.getAutoNavigationTime();

        navigationController = new MainNavigationControllerImpl(this, selectedFragment, autoNavigationEnabled,
                autoNavigationTime);

        findViewById(R.id.menu_item_room_status).setOnClickListener(v -> navigationController.navigateToStatus());
        findViewById(R.id.menu_item_room_agenda).setOnClickListener(v -> navigationController.navigateToAgenda());
        findViewById(R.id.menu_item_all_rooms).setOnClickListener(v -> navigationController.navigateToLobby());
        findViewById(R.id.menu_item_settings).setOnClickListener(v -> navigationController.openSettings());

        startNavigationController();
    }


    private void startNavigationController() {

        Long calendarId = preferencesService.getCalendarIdOfDefaultRoom();

        // if no default room set, use last selected room
        if (calendarId == -1L) {
            calendarId = preferencesService.getCalendarIdOfLastSelectedRoom();
        }

        // if no room has been selected before, dont start navigation countdown
        if (calendarId != -1L) {
            navigationController.start();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(KEY_SELECTED_MENU_ITEM, navigationController.getSelectedFragment());

        super.onSaveInstanceState(outState);
    }


    private void doEveryMinute() {

        accountService.syncCalendar();
        roomViewModel.tick();
        setClock();
    }


    private void setClock() {

        TextView clockView = findViewById(R.id.header_clock);
        clockView.setText(formatDateAndTime());
    }


    private void registerToTimeTickIntent() {

        this.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }


    private String formatDateAndTime() {

        return new SimpleDateFormat("HH:mm  |  dd.MM.yy", Locale.getDefault()).format(new Date());
    }


    @Override
    protected void onResume() {

        super.onResume();
        registerToTimeTickIntent();
        navigationController.onAutoNavigationEnabledChanged(preferencesService.isAutoNavigationEnabled());
        navigationController.onAutonavigationTimeChanged(preferencesService.getAutoNavigationTime());
        startNavigationController();
    }


    @Override
    protected void onPause() {

        super.onPause();
        unregisterReceiver(timeTickReceiver);
        navigationController.stop();
    }


    @Override
    public void onRoomSelected(long calendarId) {

        roomViewModel.setCalendarId(calendarId);
        navigationController.navigateToStatus();
        preferencesService.saveLastSelectedRoom(calendarId);
    }


    @Override
    public void setTitle(CharSequence title) {

        headerTitle.setText(title);
    }


    @Override
    public void bookNow(Duration duration) {

        roomViewModel.bookNow(duration);
    }


    @Override
    public void onBookNowDialogDismiss() {

        enableFullscreen();
    }


    @Override
    public void endNow() {

        roomViewModel.endNow();
    }


    @Override
    public void onEndNowDialogDismiss() {

        enableFullscreen();
    }


    @Override
    public void onBackPressed() {

        switch (navigationController.getSelectedFragment()) {
            case SELECTED_FRAGMENT_STATUS:
            case SELECTED_FRAGMENT_AGENDA:
                navigationController.navigateToLobby();
                break;

            case SELECTED_FRAGMENT_LOBBY:
            default:
                super.onBackPressed();
        }
    }
}
