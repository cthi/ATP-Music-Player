package com.example.chris.atp_music_player.ui.activities;


import android.support.v4.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongSubsetListAdapter;
import com.example.chris.atp_music_player.loaders.SubsetListLoader;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongSubsetActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Song>> {

    private int LOADER = 10000;
    private int mQueryType;
    private String mQueryCondition;

    private LocalPlaybackService mService;

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.song_subset_recycler_view) RecyclerView mRecyclerView;
    @InjectView(R.id.song_subset_img) ImageView mAlbumImage;

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

        mQueryCondition = getIntent().getStringExtra(Constants.QUERY_CONSTRAINT);
        mQueryType = getIntent().getIntExtra(Constants.QUERY_TYPE,0);

        getSupportActionBar().setTitle(mQueryCondition.toUpperCase());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int albumId = getIntent().getIntExtra(Constants.DATA_ALBUM_ID, 0);

        if (albumId != 0) {
            Picasso.with(this).load(AlbumArtUtils.albumArtUriFromId(albumId))
                    .placeholder(R.drawable.placeholder_aa).into(mAlbumImage);
        } else {
            Picasso.with(this).load(R.drawable.placeholder_aa).into(mAlbumImage);
        }

        getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
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

    public void pushMedia(Song song) {
        mService.play(song);
    }

    @Override
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        return new SubsetListLoader(this, mQueryType, mQueryCondition);
    }

    @Override
    public void onLoadFinished(Loader<List<Song>> loader, List<Song> result) {

        SongSubsetListAdapter adapter = new SongSubsetListAdapter(this, result);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}