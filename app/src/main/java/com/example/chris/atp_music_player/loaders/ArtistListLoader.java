package com.example.chris.atp_music_player.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.db.MediaStoreDBHelper;
import com.example.chris.atp_music_player.models.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistListLoader extends AsyncTaskLoader<List<Artist>> {

    public ArtistListLoader(Context context) {
        super(context);
    }

    public List<Artist> loadInBackground() {
        List<Artist> artistList = new ArrayList<>();
        Cursor cursor = MediaStoreDBHelper.getArtistsCursor(getContext());

        int lastArtistId = -1;

        while (cursor.moveToNext()) {

            int id = Integer.parseInt
                    (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));

            if (id == lastArtistId) {
                continue;
            }
            lastArtistId = id;

            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            int albumID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                    MediaStore.Audio.Media.ALBUM_ID)));
            artistList.add(new Artist(name, id, albumID));
        }

        cursor.close();

        return artistList;
    }
}
