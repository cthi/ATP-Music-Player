package com.example.chris.atp_music_player.services;

import android.net.Uri;

public interface MusicPlayback {

    void play(String title, String artist, Uri uri);

    void pause();

    void resume();

    void stop();

    boolean isPlaying();

    int getCurrentPlaybackPosition();

    void setCurrentPlaybackPosition(int position);

    void skipToTime(int position);
}
