package com.bosch.glm100c.easy_connect.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bosch.glm100c.easy_connect.view.MJpegView;

public class ImageListReciever extends BroadcastReceiver {
    private MJpegView mMv;
    public ImageListReciever(MJpegView _mMv){
        mMv = _mMv;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        mMv.setSource(null);
    }
}
