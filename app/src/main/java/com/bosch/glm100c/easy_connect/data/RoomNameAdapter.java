package com.bosch.glm100c.easy_connect.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//import com.example.pano.R;

import com.bosch.glm100c.easy_connect.R;

import java.util.List;

public class RoomNameAdapter extends BaseAdapter{
    List<RoomName> roomNameList;
    LayoutInflater inflter;
    public RoomNameAdapter(Context applicationContext, List<RoomName> list) {
        this.roomNameList = list;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return roomNameList.size();
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public RoomName getItem(int position){
        return roomNameList.get(position);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_rooms_names, null);
        TextView room_Name = view.findViewById(R.id.room_Name);
        room_Name.setText(roomNameList.get(i).Value);
        return view;
    }
}
