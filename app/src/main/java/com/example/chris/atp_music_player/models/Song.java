package com.example.chris.atp_music_player.models;

public class Song {
    String title;
    String artist;
    String mediaLocation;

    public Song(String title, String artist, String mediaLocation) {
        this.title = title;
        this.artist = artist;
        this.mediaLocation = mediaLocation;
    }

    public String getArtist() {

        return artist;    }


    public String getTitle() {
        return title;
    }

    public String getMediaLocation() {
        return mediaLocation;
    }
}
