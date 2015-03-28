package com.example.chris.atp_music_player.services;

import android.net.Uri;

public interface MusicPlayback {

    boolean isPlaying();

    int getCurrentPlaybackPosition();

    void setCurrentPlaybackPosition(int position);

    void play(Uri uri);

    void pause();

    void resume();

    void stop();
}
