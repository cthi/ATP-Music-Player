package com.example.chris.atp_music_player.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.db.MediaStoreDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ArtistListLoader extends AsyncTaskLoader<List<String>> {

    public ArtistListLoader(Context context) {
        super(context);
    }

    public List<String> loadInBackground() {
        List<String> artistList = new ArrayList<>();
        Cursor cursor = MediaStoreDBHelper.getArtistsCursor(getContext());

        int lastArtistId = -1;

        while (cursor.moveToNext()) {

            int id = Integer.parseInt
                    (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));

            if (id == lastArtistId) {
                continue;
            }
            lastArtistId = id;

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

            artistList.add(title);
        }

        cursor.close();
        cursor = null;

        return artistList;
    }
}
