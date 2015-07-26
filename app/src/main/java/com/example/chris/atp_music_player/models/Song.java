package com.example.chris.atp_music_player.models;

import io.realm.RealmObject;

public class Song extends RealmObject {

    private String title;
    private String artist;
    private String mediaLocation;
    private String album;
    private int albumId;

    public Song() {}

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setMediaLocation(String mediaLocation) {
        this.mediaLocation = mediaLocation;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
}
