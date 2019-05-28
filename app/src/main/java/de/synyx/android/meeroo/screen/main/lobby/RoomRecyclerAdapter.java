package de.synyx.android.meeroo.screen.main.lobby;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.synyx.android.meeroo.R;
import de.synyx.android.meeroo.domain.MeetingRoom;

import io.reactivex.Observable;

import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Max Dobler - dobler@synyx.de
 */
public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomViewHolder> {

    private List<MeetingRoom> rooms = new ArrayList<>();
    private final PublishSubject<MeetingRoom> onClickSubject = PublishSubject.create();

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lobby_room, parent, false);

        return new RoomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder roomViewHolder, int index) {

        MeetingRoom meetingRoom = rooms.get(index);

        roomViewHolder.bind(meetingRoom);
        roomViewHolder.itemView.setOnClickListener(view -> onClickSubject.onNext(meetingRoom));
    }


    @Override
    public int getItemCount() {

        return rooms.size();
    }


    public void updateRooms(List<MeetingRoom> newRooms) {

        rooms.clear();
        rooms.addAll(newRooms);
        notifyDataSetChanged();
    }


    public Observable<MeetingRoom> getItemClicks() {

        return onClickSubject;
    }
}
