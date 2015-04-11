package com.example.chris.atp_music_player.utils;

import android.content.ContentUris;
import android.net.Uri;

public class AlbumArtUtils {
    private final static Uri artworkUri = Uri.parse("content://media/external/audio/albumart");

    public static Uri albumArtUriFromId(int albumId) {
        return ContentUris.withAppendedId(artworkUri, albumId);

    }
}
