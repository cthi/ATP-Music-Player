package com.example.chris.atp_music_player.models;

public class Album {

    private String title;
    private int id;

    public Album(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

}
