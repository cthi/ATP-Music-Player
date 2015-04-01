package com.example.chris.atp_music_player.ui.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongSubsetListAdapter;
import com.example.chris.atp_music_player.db.MusicLibraryDbContract;
import com.example.chris.atp_music_player.db.MusicLibraryDbHelper;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongSubsetActivity extends BaseActivity {

    private LocalPlaybackService mService;

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.song_subset_recycler_view) RecyclerView mRecyclerView;

    private boolean mServiceBound;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            LocalPlaybackService.LocalBinder binder = (LocalPlaybackService.LocalBinder) service;
            mService = binder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mServiceBound = false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_subset);

        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SQLiteDatabase db = new MusicLibraryDbHelper(this).getReadableDatabase();

        String where = "";
        String condition = getIntent().getStringExtra(Constants.QUERY_CONSTRAINT);
        int queryType = getIntent().getIntExtra(Constants.QUERY_TYPE,0);

        getSupportActionBar().setTitle(condition.toUpperCase());

        if (queryType == Constants.QUERY_TYPE_ALBUM) {
            where = " WHERE album='" + condition + "'";
        } else if (queryType == Constants.QUERY_TYPE_ARTIST) {
            where = " WHERE artist='" + condition + "'";
        }

        String query = "SELECT id _id, title, artist, album, data  FROM " + MusicLibraryDbContract.MusicLibraryEntry.TABLE_NAME + where;

        Cursor cursor = db.rawQuery(query,null);
        SongSubsetListAdapter adapter = new SongSubsetListAdapter(cursor, this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(this, LocalPlaybackService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        super.onStop();

        if (mServiceBound) {
            unbindService(mConnection);
            mServiceBound = false;
        }
    }

    public void pushMedia(String title, String artist, Uri uri) {
        mService.play(title, artist, uri);
    }
}
