package de.synyx.android.meeroo.screen.main.lobby;

import android.arch.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.screen.ScreenSize;
import de.synyx.android.meeroo.screen.main.MainActivity;
import de.synyx.android.meeroo.screen.main.status.TimeTickReceiver;

import io.reactivex.disposables.Disposable;

import static de.synyx.android.meeroo.util.ui.ScreenUtil.getSizeOfScreen;


public class LobbyFragment extends Fragment {

    private LobbyViewModel viewModel;
    private RoomSelectionListener roomSelectionListener;
    private Disposable roomSelectionObservable;
    private TimeTickReceiver timeTickReceiver;

    public LobbyFragment() {

        // Required empty public constructor
    }

    public static LobbyFragment newInstance() {

        LobbyFragment fragment = new LobbyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lobby, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(LobbyViewModel.class);
        timeTickReceiver = new TimeTickReceiver();
        timeTickReceiver.getTicks().subscribe(ignored -> viewModel.tick());
        setRoomRecyclerView(view);
    }


    private void setRoomRecyclerView(View view) {

        RecyclerView roomsRecyclerView = view.findViewById(R.id.roomRecyclerView);
        roomsRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                calculateGridSpan(getSizeOfScreen(getActivity())));
        roomsRecyclerView.setLayoutManager(layoutManager);

        RoomRecyclerAdapter roomRecyclerAdapter = new RoomRecyclerAdapter();
        roomsRecyclerView.setAdapter(roomRecyclerAdapter);

        observeItemClicks(roomRecyclerAdapter);

        viewModel.getRooms().observe(this, roomRecyclerAdapter::updateRooms);
    }


    private static int calculateGridSpan(ScreenSize screenSize) {

        switch (screenSize) {
            case XSMALL:
                return 2;

            case SMALL:
                return 3;

            case MEDIUM:
                return 4;

            case LARGE:
            case XLARGE:
                return 5;

            default:
                return 4;
        }
    }


    private void observeItemClicks(RoomRecyclerAdapter roomRecyclerAdapter) {

        roomSelectionObservable =
            roomRecyclerAdapter.getItemClicks() //
            .subscribe(room -> roomSelectionListener.onRoomSelected(room.getCalendarId()));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        roomSelectionListener = activity;
        activity.setTitle(getString(R.string.header_title_lobby));
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        roomSelectionObservable.dispose();
    }


    @Override
    public void onResume() {

        super.onResume();

        getActivity().registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }


    @Override
    public void onPause() {

        super.onPause();
        getActivity().unregisterReceiver(timeTickReceiver);
    }

    public interface RoomSelectionListener {

        void onRoomSelected(long id);
    }
}
