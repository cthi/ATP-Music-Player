package com.example.chris.atp_music_player.playback

import android.content.Context
import android.provider.MediaStore
import com.example.chris.atp_music_player.*
import rx.Observable
import rx.lang.kotlin.observable

class MediaProvider(val context: Context) {
    fun getAllSongs(): Observable<Song> = observable { subscriber ->
        val cursor = MediaStoreDb.getSongsCursor(context)

        while (cursor.moveToNext()) {
            val title = cursor.valueOf(MediaStore.Audio.Media.TITLE)
            val artist = cursor.valueOf(MediaStore.Audio.Media.ARTIST)
            val mediaLocation = cursor.valueOf(MediaStore.Audio.Media.DATA)
            val album = cursor.valueOf(MediaStore.Audio.Media.ALBUM)
            val albumId = cursor.valueOf(MediaStore.Audio.Media.ALBUM_ID).toInt()

            subscriber.onNext(Song(title, artist, mediaLocation, album, albumId))
        }

        cursor.close()
        subscriber.onCompleted()
    }

    fun getSongs(queryType: Int, queryCondition: String): Observable<Song> = observable { subscriber ->
        val selectionArgs = arrayOf(queryCondition)
        val selectionClause = when (queryType) {
            Constants.QUERY_TYPE_ALBUM -> "album=?"
            Constants.QUERY_TYPE_ARTIST -> "artist=?"
            else -> "album=?"
        }

        val cursor = MediaStoreDb.getSongsCursor(context, selectionClause, selectionArgs)

        while (cursor.moveToNext()) {
            val title = cursor.valueOf(MediaStore.Audio.Media.TITLE)
            val artist = cursor.valueOf(MediaStore.Audio.Media.ARTIST)
            val mediaLocation = cursor.valueOf(MediaStore.Audio.Media.DATA)
            val album = cursor.valueOf(MediaStore.Audio.Media.ALBUM)
            val albumId = cursor.valueOf(MediaStore.Audio.Media.ALBUM_ID).toInt()

            subscriber.onNext(Song(title, artist, mediaLocation, album, albumId))
        }

        cursor.close()
        subscriber.onCompleted()
    }

    fun getAllAlbums(): Observable<Album> = observable { subscriber ->
        val cursor = MediaStoreDb.getAlbumsCursor(context)

        var lastAlbumId = -1

        while (cursor.moveToNext()) {
            val id = cursor.valueOf(MediaStore.Audio.Media.ALBUM_ID).toInt()

            if (id == lastAlbumId) {
                continue
            }
            lastAlbumId = id

            val title = cursor.valueOf(MediaStore.Audio.Media.ALBUM)
            subscriber.onNext(Album(title, id))
        }

        cursor.close()
        subscriber.onCompleted()
    }

    fun getAllArtists(): Observable<Artist> = observable { subscriber ->
        val cursor = MediaStoreDb.getArtistsCursor(context)

        var lastArtistId = -1

        while (cursor.moveToNext()) {
            val id = cursor.valueOf(MediaStore.Audio.Media.ARTIST_ID).toInt()

            if (id == lastArtistId) {
                continue
            }
            lastArtistId = id

            val name = cursor.valueOf(MediaStore.Audio.Media.ARTIST)
            val albumId = cursor.valueOf(MediaStore.Audio.Media.ALBUM_ID).toInt()

            subscriber.onNext(Artist(name, id, albumId))
        }

        cursor.close()
        subscriber.onCompleted()
    }

    fun getAllGenres(): Observable<Genre> = observable { subscriber ->
        val cursor = MediaStoreDb.getGenresCursor(context)

        while (cursor.moveToNext()) {
            val title = cursor.valueOf(MediaStore.Audio.Genres.NAME)
            val id = cursor.valueOf(MediaStore.Audio.Genres._ID).toInt()

            subscriber.onNext(Genre(title, id))
        }

        cursor.close()
        subscriber.onCompleted()
    }

    fun getSongsWithGenre(genreId: Int): Observable<Song> = observable { subscriber ->
        val cursor = MediaStoreDb.getSongsFromGenreCursor(context, genreId)

        while (cursor.moveToNext()) {
            val title = cursor.valueOf(MediaStore.Audio.Media.TITLE)
            val artist = cursor.valueOf(MediaStore.Audio.Media.ARTIST)
            val mediaLocation = cursor.valueOf(MediaStore.Audio.Media.DATA)
            val album = cursor.valueOf(MediaStore.Audio.Media.ALBUM)
            val albumId = cursor.valueOf(MediaStore.Audio.Media.ALBUM_ID).toInt()

            subscriber.onNext(Song(title, artist, mediaLocation, album, albumId))
        }

        cursor.close()
        subscriber.onCompleted()
    }
}