package com.example.chris.atp_music_player.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicQueryLoader extends AsyncTaskLoader<Cursor> {

    final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    final String SONG_FILTER =  MediaStore.Audio.Media.IS_MUSIC + "!= 0";
    final String[] PROJECTION = { MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media._ID };

    public MusicQueryLoader(Context context){
        super(context);
    }

    @Override
    public Cursor loadInBackground(){

        ContentResolver contentResolver = getContext().getContentResolver();
        return contentResolver.query(MEDIA_URI, PROJECTION, SONG_FILTER, null,null);
    }

    @Override
    public void deliverResult(Cursor cursor){
        if (isReset()) {
            cursor.close();
        }

        if (isStarted()) {
            super.deliverResult(cursor);
        }
    }

    @Override
    protected void onStopLoading(){
        cancelLoad();
    }

    @Override
    protected void onReset(){

        onStopLoading();
    }

    @Override
    public void onCanceled(Cursor cursor){
        super.onCanceled(cursor);

        cursor.close();
    }
}
