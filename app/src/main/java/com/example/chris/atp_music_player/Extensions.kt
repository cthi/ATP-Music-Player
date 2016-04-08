package com.example.chris.atp_music_player

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri

fun Song.albumArtUri(): Uri {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId.toLong());
}

fun Artist.albumArtUri(): Uri {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId.toLong());
}

fun Album.albumArtUri(): Uri {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), id.toLong());
}

fun Cursor.valueOf(column: String): String {
    return getString(getColumnIndex(column))
}