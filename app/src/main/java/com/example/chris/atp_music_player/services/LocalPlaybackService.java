package com.example.chris.atp_music_player.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.receivers.ReceiverMessages;

import java.io.IOException;

public class LocalPlaybackService extends Service implements MusicPlayback,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final float VOLUME_DUCK = 0.2f;
    public static final float VOLUME_NORMAL = 1.0f;

    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_STOPPED = 2;

    private int mPlaybackState;
    private boolean mReceiverRegistered;
    private volatile int mCurrentPlaybackPosition;

    private BroadcastReceiver mMusicIntentReceiver;
    private IntentFilter mAudioNoisyIntentFilter;
    private IBinder mBinder;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private String mLastTitle;
    private String mLastArtist;
    private Uri mLastPlayed;

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
    public void onCreate() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mBinder = new LocalBinder();

        mAudioNoisyIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        mMusicIntentReceiver = new MusicIntentReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        releaseResources();

        unregisterMusicIntentReceiver();
    }

    @Override
    public void play(String title, String artist, Uri uri) {

        if (requestAudioFocus()) {
            try {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setWakeMode(this.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                    mMediaPlayer.setOnCompletionListener(this);
                    mMediaPlayer.setOnPreparedListener(this);
                    mMediaPlayer.setOnErrorListener(this);
                } else {
                    mMediaPlayer.reset();
                }

                mLastPlayed = uri;
                mLastTitle = title;
                mLastArtist = artist;

                mMediaPlayer.setDataSource(this, mLastPlayed);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepareAsync();

                registerMusicIntentReceiver();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            releaseResources();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mCurrentPlaybackPosition = mMediaPlayer.getCurrentPosition();
        }

        releaseAudioFocus();
        unregisterMusicIntentReceiver();

        mPlaybackState = STATE_PAUSED;
    }

    @Override
    public void resume() {
        if (requestAudioFocus()) {
            if (mMediaPlayer == null && mPlaybackState == STATE_PAUSED) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setWakeMode(this.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                mMediaPlayer.setOnPreparedListener(this);
            } else {
                registerMusicIntentReceiver();

                if (mPlaybackState == STATE_PAUSED) {
                    skipToTime(mCurrentPlaybackPosition);
                }

                mMediaPlayer.start();
            }
        }
    }

    @Override
    public void stop() {
        pause();
        releaseResources();

        mPlaybackState = STATE_STOPPED;
        mLastPlayed = null;
        mCurrentPlaybackPosition = 0;
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
    public void skipToTime(int position) {
        if (isPlaying()) {
            mMediaPlayer.seekTo(position);
        }
        mCurrentPlaybackPosition = position;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();

        mPlaybackState = STATE_PLAYING;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.putExtra(ReceiverMessages.PLAYBACK_INTENT_TAG, ReceiverMessages.STREAM_ENDED);
        sendBroadcast(intent);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            mMediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK);
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            pause();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            stop();
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            mMediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL);
        }
    }

    public void releaseResources() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public boolean requestAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public boolean releaseAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(this);
    }

    private void registerMusicIntentReceiver() {
        if (!mReceiverRegistered) {
            registerReceiver(mMusicIntentReceiver, mAudioNoisyIntentFilter);
            mReceiverRegistered = true;
        }
    }

    private void unregisterMusicIntentReceiver() {
        if (mReceiverRegistered) {
            unregisterReceiver(mMusicIntentReceiver);
            mReceiverRegistered = false;
        }
    }

    public Song getLastSong() {
        return new Song(mLastTitle, mLastArtist);
    }
}