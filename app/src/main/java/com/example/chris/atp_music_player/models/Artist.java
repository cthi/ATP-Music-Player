package com.example.chris.atp_music_player.models;

public class Artist {

    private String name;
    private int ID;
    private int albumID;

    public Artist(String name, int ID, int albumID) {

        this.name = name;
        this.ID = ID;
        this.albumID = albumID;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public int getAlbumID() {
        return albumID;
    }
}
