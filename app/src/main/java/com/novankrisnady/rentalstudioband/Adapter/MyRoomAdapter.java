package com.novankrisnady.rentalstudioband.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.novankrisnady.rentalstudioband.Model.Room;
import com.novankrisnady.rentalstudioband.R;

import java.util.List;

public class MyRoomAdapter extends RecyclerView.Adapter<MyRoomAdapter.MyViewHolder> {

    Context context;
    List<Room> roomList;

    public MyRoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_room,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_room_name.setText(roomList.get(i).getName());
        myViewHolder.ratingBar.setRating((float)roomList.get(i).getRating());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_room_name;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_room_name = (TextView)itemView.findViewById(R.id.txt_room_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_room);
        }
    }
}
