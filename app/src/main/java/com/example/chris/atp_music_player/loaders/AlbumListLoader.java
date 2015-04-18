package com.example.chris.atp_music_player.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.db.MediaStoreDBHelper;
import com.example.chris.atp_music_player.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumListLoader extends AsyncTaskLoader<List<Album>> {

    public AlbumListLoader(Context context) {
        super(context);
    }

    public List<Album> loadInBackground() {
        List<Album> albumList = new ArrayList<>();
        Cursor cursor = MediaStoreDBHelper.getAlbumsCursor(getContext());

        int lastAlbumId = -1;

        while (cursor.moveToNext()) {

            int id = Integer.parseInt
                    (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));

            if (id == lastAlbumId) {
                continue;
            }
            lastAlbumId = id;

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

            albumList.add(new Album(title, id));
        }

        cursor.close();
        cursor = null;

        return albumList;
    }
}
