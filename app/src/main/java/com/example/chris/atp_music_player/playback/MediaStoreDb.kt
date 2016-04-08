package com.example.chris.atp_music_player.playback

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

object MediaStoreDb {
    private val MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    private val GENRE_URI = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
    private val SONG_PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATA)

    private val SONG_SELECTION_CLAUSE = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
    private val SONG_ORDER = MediaStore.Audio.Media.TITLE + " ASC"
    private val ARTIST_PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM_ID
    )
    private val ARTIST_ORDER = MediaStore.Audio.Media.ARTIST + " ASC"
    private val ALBUM_PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
    )
    private val ALBUM_ORDER = MediaStore.Audio.Media.ALBUM + " ASC"
    private val GENRE_PROJECTION = arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
    )
    private val GENRE_ORDER = MediaStore.Audio.Genres.NAME + " ASC"

    fun getSongsCursor(context: Context): Cursor {
        return context.contentResolver.query(MEDIA_URI, SONG_PROJECTION, SONG_SELECTION_CLAUSE, null, SONG_ORDER)
    }

    fun getArtistsCursor(context: Context): Cursor {
        return context.contentResolver.query(MEDIA_URI, ARTIST_PROJECTION, SONG_SELECTION_CLAUSE, null, ARTIST_ORDER)
    }

    fun getAlbumsCursor(context: Context): Cursor {
        return context.contentResolver.query(MEDIA_URI, ALBUM_PROJECTION, SONG_SELECTION_CLAUSE, null, ALBUM_ORDER)
    }

    fun getSongsCursor(context: Context, selectionClause: String, selectionArgs: Array<String>): Cursor {
        return context.contentResolver.query(MEDIA_URI, SONG_PROJECTION, selectionClause, selectionArgs, SONG_ORDER)
    }

    fun getGenresCursor(context: Context): Cursor {
        return context.contentResolver.query(GENRE_URI, GENRE_PROJECTION, null, null, GENRE_ORDER)
    }

    fun getSongsFromGenreCursor(context: Context, genreId: Int): Cursor {
        val GENRE_MEMBERS_URI = MediaStore.Audio.Genres.Members.getContentUri("external", genreId.toLong())
        return context.contentResolver.query(GENRE_MEMBERS_URI, SONG_PROJECTION, null, null, SONG_ORDER)
    }
}