package com.example.chris.atp_music_player.ui.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.TypedArray;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.services.LocalPlaybackService;

import java.util.List;

public abstract class BaseServiceActivity extends ActionBarActivity {

    protected LocalPlaybackService mService;
    protected boolean mServiceBound;

    public int getColorPrimary() {
        TypedArray tmp = obtainStyledAttributes(new TypedValue().data,
                new int[]{R.attr.colorPrimary});
        int color = tmp.getColor(0, 0);
        tmp.recycle();

        return color;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalPlaybackService.LocalBinder binder = (LocalPlaybackService.LocalBinder) service;
            mService = binder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, LocalPlaybackService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mServiceBound) {
            unbindService(mConnection);
        }
    }

    public void pushMedia(List<Song> songList, int position) {
        mService.play(songList, position);
    }
}