package com.example.chris.atp_music_player.models;


public class Song {

    String title;
    String artist;
    String album;
    String albumId;
    String duration;
    String track;
    String year;
    String data;
    String dateAdded;
    String dateModified;
    String entryId;

    public Song(String title, String artist, String album, String albumId, String duration, String track, String year, String data, String dateAdded, String dateModified, String entryId) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.track = track;
        this.year = year;
        this.data = data;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.entryId = entryId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getDuration() {
        return duration;
    }

    public String getTrack() {
        return track;
    }

    public String getYear() {
        return year;
    }

    public String getData() {
        return data;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getEntryId() {
        return entryId;
    }
}
