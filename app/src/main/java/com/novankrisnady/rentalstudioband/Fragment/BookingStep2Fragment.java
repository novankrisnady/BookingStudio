package com.novankrisnady.rentalstudioband.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.novankrisnady.rentalstudioband.Adapter.MyRoomAdapter;
import com.novankrisnady.rentalstudioband.Common.Common;
import com.novankrisnady.rentalstudioband.Common.SpacesItemDecoration;
import com.novankrisnady.rentalstudioband.Model.Room;
import com.novankrisnady.rentalstudioband.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_room)
    RecyclerView recycler_room;

    private BroadcastReceiver roomDoneRecevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Room> roomArrayList = intent.getParcelableArrayListExtra(Common.KEY_ROOM_LOAD_DONE);
            //Create adapter late
            MyRoomAdapter adapter = new MyRoomAdapter(getContext(),roomArrayList);
            recycler_room.setAdapter(adapter);


        }
    };

    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(roomDoneRecevier,new IntentFilter(Common.KEY_ROOM_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(roomDoneRecevier);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_two,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        initView();
        
        return itemView;
    }

    private void initView() {
        recycler_room.setHasFixedSize(true);
        recycler_room.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_room.addItemDecoration(new SpacesItemDecoration(4));
    }
}
