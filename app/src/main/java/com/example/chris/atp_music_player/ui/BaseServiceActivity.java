package com.example.chris.atp_music_player.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.example.chris.atp_music_player.Constants;
import com.example.chris.atp_music_player.playback.PlaybackService;

public abstract class BaseServiceActivity extends AppCompatActivity {
    protected PlaybackService mService;
    protected boolean mServiceBound;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PlaybackService.LocalBinder binder = (PlaybackService.LocalBinder) service;
            mService = binder.getService();
            mServiceBound = true;
            onServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, PlaybackService.class);
        intent.setAction(Constants.INSTANCE.getPLAYBACK_STOP_FOREGROUND());
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
}
