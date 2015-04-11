package com.example.chris.atp_music_player.models;

public class Song {
    String title;
    String artist;
    String mediaLocation;
    String album;
    int albumId;

    public Song(String title, String artist, String mediaLocation, int albumId, String album) {
        this.title = title;
        this.artist = artist;
        this.mediaLocation = mediaLocation;
        this.albumId = albumId;
        this.album = album;
    }

    public String getArtist() {

        return artist;    }


    public String getTitle() {
        return title;
    }

    public String getMediaLocation() {
        return mediaLocation;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getAlbum(){
        return album;
    }
}
