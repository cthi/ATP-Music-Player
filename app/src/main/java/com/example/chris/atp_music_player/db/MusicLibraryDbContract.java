package com.example.chris.atp_music_player.db;

import android.provider.BaseColumns;

public class MusicLibraryDbContract {

    public MusicLibraryDbContract(){}

    public static abstract class MusicLibraryEntry implements BaseColumns {
        public static final String TABLE_NAME = "musicTable";
        public static final String SONG_TITLE = "title";
        public static final String SONG_ARTIST = "artist";
        public static final String SONG_ALBUM = "album";
        public static final String SONG_ALBUM_ID = "albumId";
        public static final String SONG_DURATION = "duration";
        public static final String SONG_TRACK = "track";
        public static final String SONG_YEAR = "year";
        public static final String SONG_DATA = "data";
        public static final String SONG_DATE_ADDED = "dateAdded";
        public static final String SONG_DATE_MODIFIED = "dateModified";
        public static final String ENTRY_ID = "id";
    }
}
