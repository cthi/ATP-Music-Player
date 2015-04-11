package com.example.chris.atp_music_player.services;

import com.example.chris.atp_music_player.models.Song;

public interface MusicPlayback {

    void play(Song song);

    void pause();

    void resume();

    void stop();

    boolean isPlaying();

    int getCurrentPlaybackPosition();

    void setCurrentPlaybackPosition(int position);

    void skipToTime(int position);
}
