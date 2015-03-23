package com.example.chris.atp_music_player.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicLibraryDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MusicLibrary.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MusicLibraryDbContract.MusicLibraryEntry.TABLE_NAME + " (" +
                    MusicLibraryDbContract.MusicLibraryEntry.ENTRY_ID + " INTEGER PRIMARY KEY," +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_TITLE + TEXT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_ARTIST + TEXT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_ALBUM + TEXT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_ALBUM_ID + INT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_DURATION + INT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_TRACK + INT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_YEAR + INT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_DATA + TEXT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_DATE_ADDED + TEXT_TYPE + COMMA_SEP +
                    MusicLibraryDbContract.MusicLibraryEntry.SONG_DATE_MODIFIED + INT_TYPE +" )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MusicLibraryDbContract.MusicLibraryEntry.TABLE_NAME;

    public MusicLibraryDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
