package com.example.chris.atp_music_player.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.chris.atp_music_player.db.MediaStoreDBHelper;
import com.example.chris.atp_music_player.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongListLoader extends AsyncTaskLoader<List<Song>> {

    public SongListLoader(Context context) {
        super(context);
    }

    public List<Song> loadInBackground() {
        List<Song> songList = new ArrayList<>();
        Cursor cursor = MediaStoreDBHelper.getSongsCursor(getContext());

        while (cursor.moveToNext()) {

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String mediaLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            int albumId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));

            songList.add(new Song(title, artist, mediaLocation, albumId,album));
        }

        cursor.close();
        cursor = null;

        return songList;
    }
}
