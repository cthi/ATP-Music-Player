package com.example.chris.atp_music_player.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;

import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.List;

public abstract class BaseServiceActivity extends ActionBarActivity {
    protected LocalPlaybackService mService;
    protected boolean mServiceBound;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalPlaybackService.LocalBinder binder = (LocalPlaybackService.LocalBinder) service;
            mService = binder.getService();
            onServiceBound();
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
        intent.setAction(Constants.PLAYBACK_STOP_FOREGROUND);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mServiceBound) {
            unbindService(mConnection);
        }
    }

    void onServiceBound() {
    }

    public void pushMedia(List<Song> songList, int position) {
        mService.play(songList, position);
    }

    public void pushMediaDontQueue(List<Song> songList, int position) {
        mService.playDontQueue(songList, position);
    }
}
