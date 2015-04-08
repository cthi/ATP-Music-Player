package com.example.chris.atp_music_player.loaders;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;

import com.example.chris.atp_music_player.db.MediaStoreDBHelper;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SubsetListLoader extends AsyncTaskLoader<List<Song>> {

    String mSelectionClause;
    String[] mSelectionArgs;

    public SubsetListLoader(Context context, int queryType, String queryCondition) {
        super(context);
        constructQuery(queryType, queryCondition);
    }

    public List<Song> loadInBackground() {
        List<Song> songList = new ArrayList<>();
        Cursor cursor = MediaStoreDBHelper.getSongsCursor(getContext(), mSelectionClause, mSelectionArgs);

        while (cursor.moveToNext()) {

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String mediaLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            songList.add(new Song(title, artist, mediaLocation));
        }

        cursor.close();
        cursor = null;

        return songList;
    }

    public void constructQuery(int queryType, String queryCondition) {
        if (queryType == Constants.QUERY_TYPE_ALBUM) {
            mSelectionClause = "album=?";
        } else if (queryType == Constants.QUERY_TYPE_ARTIST) {
            mSelectionClause = "artist=?";
        }
        mSelectionArgs = new String[1];
        mSelectionArgs[0] = queryCondition;
    }
}
