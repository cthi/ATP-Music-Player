package com.example.chris.atp_music_player.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

public class LocalPlaybackService extends Service implements MusicPlayback,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener {
    public static final float VOLUME_DUCK = 0.2f;
    public static final float VOLUME_NORMAL = 1.0f;

    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_FOCUSED = 2;

    private int mPlaybackState;
    private int mAudioFocusState;
    private boolean mPlaybackPaused = false;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private IBinder mBinder = new LocalBinder();
    private volatile int mCurrentPlaybackPosition;

    public class LocalBinder extends Binder {
        public LocalPlaybackService getService() {
            return LocalPlaybackService.this;
        }
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                pause();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            releaseResources();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (mPlaybackState == PlaybackState.STATE_PLAYING) {
                pause();
            }
        }
    }

    public void updateMediaPlayerState() {
        if (mAudioFocusState == AUDIO_NO_FOCUS_NO_DUCK && mPlaybackState == PlaybackState.STATE_PLAYING) {
            pause();
        } else if (mAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK) {
            mMediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK);
        } else {
            mMediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL);
        }

        //if (mAudioFocusState != AUDIO_NO_FOCUS_NO_DUCK && mPlaybackPaused) {
        //}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public int getCurrentPlaybackPosition() {
        return mCurrentPlaybackPosition;
    }

    @Override
    public void setCurrentPlaybackPosition(int position) {
        mCurrentPlaybackPosition = position;
    }

    @Override
    public void play(Uri uri) {

        if (requestAudioFocus()) {
            try {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setWakeMode(this.getApplicationContext(),
                            PowerManager.PARTIAL_WAKE_LOCK);
                    mMediaPlayer.setOnPreparedListener(this);
                } else {
                    mMediaPlayer.reset();
                }
                mMediaPlayer.setDataSource(this, uri);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            releaseResources();
        }
    }

    public boolean requestAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public boolean abandonAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void pause() {
        stopForeground(true);

    }

    @Override
    public void stop(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            releaseResources();
        }

        abandonAudioFocus();

    }
    @Override
    public void onDestroy() {
        releaseResources();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("PROGRESS", "DFD");
        mMediaPlayer.start();
    }

    public void releaseResources() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void destroyCallbacks(){

    }
}
