package com.example.chris.atp_music_player.provider;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.models.Album;
import com.example.chris.atp_music_player.models.Artist;
import com.example.chris.atp_music_player.models.Genre;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class MusicProvider {

    private Context mContext;

    public MusicProvider(Context context) {
        this.mContext = context;
    }

    public Observable<List<Song>> getAllSongs() {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {
                try {
                    List<Song> songList = new ArrayList<>();
                    Cursor cursor = MediaStoreDb.getSongsCursor(mContext);

                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String mediaLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        int albumId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));

                        songList.add(new Song(title, artist, mediaLocation, albumId, album));
                    }

                    cursor.close();

                    subscriber.onNext(songList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Song>> getSongs(final int queryType, final String queryCondition) {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {
                try {
                    String mSelectionClause;
                    String[] mSelectionArgs;

                    if (queryType == Constants.QUERY_TYPE_ALBUM) {
                        mSelectionClause = "album=?";
                    } else if (queryType == Constants.QUERY_TYPE_ARTIST) {
                        mSelectionClause = "artist=?";
                    } else {
                        mSelectionClause = "album=?";
                    }
                    mSelectionArgs = new String[1];
                    mSelectionArgs[0] = queryCondition;

                    List<Song> songList = new ArrayList<>();
                    Cursor cursor = MediaStoreDb.getSongsCursor(mContext, mSelectionClause,
                            mSelectionArgs);

                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String mediaLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        int albumId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));

                        songList.add(new Song(title, artist, mediaLocation, albumId, album));
                    }

                    cursor.close();

                    subscriber.onNext(songList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Album>> getAllAlbums() {
        return Observable.create(new Observable.OnSubscribe<List<Album>>() {
            @Override
            public void call(Subscriber<? super List<Album>> subscriber) {
                try {
                    List<Album> albumList = new ArrayList<>();
                    Cursor cursor = MediaStoreDb.getAlbumsCursor(mContext);

                    int lastAlbumId = -1;

                    while (cursor.moveToNext()) {
                        int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore
                                .Audio.Media.ALBUM_ID)));

                        if (id == lastAlbumId) {
                            continue;
                        }
                        lastAlbumId = id;

                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                                .ALBUM));

                        albumList.add(new Album(title, id));
                    }

                    cursor.close();

                    subscriber.onNext(albumList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Artist>> getAllArtists() {
        return Observable.create(new Observable.OnSubscribe<List<Artist>>() {
            @Override
            public void call(Subscriber<? super List<Artist>> subscriber) {
                try {
                    List<Artist> artistList = new ArrayList<>();
                    Cursor cursor = MediaStoreDb.getArtistsCursor(mContext);

                    int lastArtistId = -1;

                    while (cursor.moveToNext()) {
                        int id = Integer.parseInt
                                (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));

                        if (id == lastArtistId) {
                            continue;
                        }
                        lastArtistId = id;

                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        int albumID = Integer.parseInt(cursor.getString(cursor.getColumnIndex
                                (MediaStore.Audio.Media.ALBUM_ID)));
                        artistList.add(new Artist(name, id, albumID));
                    }

                    cursor.close();

                    subscriber.onNext(artistList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Genre>> getAllGenres() {
        return Observable.create(new Observable.OnSubscribe<List<Genre>>() {
            @Override
            public void call(Subscriber<? super List<Genre>> subscriber) {
                try {
                    List<Genre> genreList = new ArrayList<>();
                    Cursor cursor = MediaStoreDb.getGenresCursor(mContext);

                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));
                        int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres._ID)));

                        genreList.add(new Genre(title, id));
                    }

                    cursor.close();

                    subscriber.onNext(genreList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Song>> getSongsWithGenre(final int genreId) {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {
                try {
                    List<Song> songList = new ArrayList<>();

                    Cursor cursor = MediaStoreDb.getSongsFromGenreCursor(mContext, genreId);

                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String mediaLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        int albumId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));

                        songList.add(new Song(title, artist, mediaLocation, albumId, album));
                    }

                    cursor.close();

                    subscriber.onNext(songList);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
