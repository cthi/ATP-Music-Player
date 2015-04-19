package com.example.chris.atp_music_player.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.chris.atp_music_player.db.MediaStoreDBHelper;
import com.example.chris.atp_music_player.models.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreListLoader extends AsyncTaskLoader<List<Genre>> {

    public GenreListLoader(Context context) {
        super(context);
    }

    public List<Genre> loadInBackground() {
        List<Genre> genreList = new ArrayList<>();
        Cursor cursor = MediaStoreDBHelper.getGenresCursor(getContext());

        while (cursor.moveToNext()) {

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres._ID)));

            genreList.add(new Genre(title, id));
        }

        cursor.close();
        cursor = null;

        return genreList;
    }
}
