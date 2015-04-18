package com.example.chris.atp_music_player.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.db.MediaStoreDBHelper;

import java.util.ArrayList;
import java.util.List;

public class GenreListLoader extends AsyncTaskLoader<List<String>> {

    public GenreListLoader(Context context) {
        super(context);
    }

    public List<String> loadInBackground() {
        List<String> genreList = new ArrayList<>();
        Cursor cursor = MediaStoreDBHelper.getGenresCursor(getContext());

        while (cursor.moveToNext()) {

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));

            genreList.add(title);
        }

        cursor.close();
        cursor = null;

        return genreList;
    }
}
