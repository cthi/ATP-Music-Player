package com.example.chris.atp_music_player.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.utils.Constants;

public class MediaStoreDBHelper {
    final static Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    final static String[] PROJECTION = {MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media._ID};

    final static String[] SONG_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
    };
    final static String SONG_SELECTION_CLAUSE = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
    final static String SONG_ORDER = MediaStore.Audio.Media.TITLE + " ASC";

    final static String[] ARTIST_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
    };
    final static String ARTIST_ORDER = MediaStore.Audio.Media.ARTIST + " ASC";

    final static String[] ALBUM_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
    };
    final static String ALBUM_ORDER = MediaStore.Audio.Media.ALBUM + " ASC";

    public static Cursor getSongsCursor(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(MEDIA_URI, SONG_PROJECTION, SONG_SELECTION_CLAUSE, null, SONG_ORDER);
    }

    public static Cursor getArtistsCursor(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(MEDIA_URI, ARTIST_PROJECTION, SONG_SELECTION_CLAUSE, null, ARTIST_ORDER);
    }

    public static Cursor getAlbumsCursor(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(MEDIA_URI, ALBUM_PROJECTION, SONG_SELECTION_CLAUSE, null, ALBUM_ORDER);
    }

    public static Cursor getSongsCursor(Context context, String selectionClause, String[] selectionArgs) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(MEDIA_URI, SONG_PROJECTION, selectionClause, selectionArgs, SONG_ORDER);
    }
}
