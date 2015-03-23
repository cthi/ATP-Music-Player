package com.example.chris.atp_music_player.loaders;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.db.MusicLibraryDbContract;
import com.example.chris.atp_music_player.db.MusicLibraryDbHelper;

public class MusicQueryLoader extends AsyncTaskLoader<Boolean> {

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
    public Boolean loadInBackground(){

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(MEDIA_URI, PROJECTION, SONG_FILTER, null,null);

        MusicLibraryDbHelper dbHelper = new MusicLibraryDbHelper(getContext());
        SQLiteDatabase musicLibraryDb = dbHelper.getWritableDatabase();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            ContentValues values = new ContentValues();

            values.put(MusicLibraryDbContract.MusicLibraryEntry.ENTRY_ID, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_TITLE, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_ARTIST, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_ALBUM, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_ALBUM_ID, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_DURATION, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_TRACK, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_YEAR, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_DATA, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_DATE_ADDED, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
            values.put(MusicLibraryDbContract.MusicLibraryEntry.SONG_DATE_MODIFIED, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)));

            musicLibraryDb.insert(MusicLibraryDbContract.MusicLibraryEntry.TABLE_NAME, null, values);
        }

        cursor.close();

        return true;
    }

    @Override
    public void deliverResult(Boolean result){
        /*if (isReset()) {
            cursor.close();
        }

        if (isStarted()) {
            super.deliverResult(cursor);
        }*/
        super.deliverResult(result);
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
    public void onCanceled(Boolean result){
        super.onCanceled(result);
    }
}
