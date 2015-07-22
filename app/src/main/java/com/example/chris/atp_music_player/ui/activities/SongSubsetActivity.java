package com.example.chris.atp_music_player.ui.activities;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.chris.atp_music_player.ATPApplication;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongSubsetListAdapter;
import com.example.chris.atp_music_player.loaders.SubsetListLoader;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongSubsetActivity extends BaseServiceActivity
        implements LoaderManager.LoaderCallbacks<List<Song>> {

    private int LOADER = 10000;
    private int mQueryType;
    private String mQueryCondition;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.song_subset_recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.song_subset_img)
    ImageView mAlbumImage;

    private boolean shouldStartForeground = true;
    private SongSubsetListAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_subset);

        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mQueryCondition = getIntent().getStringExtra(Constants.QUERY_CONSTRAINT);
        mQueryType = getIntent().getIntExtra(Constants.QUERY_TYPE, 0);

        getSupportActionBar().setTitle(mQueryCondition.toUpperCase());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new SongSubsetListAdapter(this, new ArrayList<Song>());
        mRecyclerView.setAdapter(mAdapter);

        int albumId = getIntent().getIntExtra(Constants.DATA_ALBUM_ID, 0);

        if (albumId != 0) {
            Glide.with(this).load(AlbumArtUtils.albumArtUriFromId(albumId))
                    .fitCenter()
                    .placeholder(R.drawable.placeholder_aa).into(mAlbumImage);
        } else {
            Glide.with(this).
                    load(R.drawable.placeholder_aa).into(mAlbumImage);
        }

        getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        shouldStartForeground = false;
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = new Intent(this, LocalPlaybackService.class);
        intent.setAction(Constants.PLAYBACK_STOP_FOREGROUND);
        startService(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        ATPApplication.subActivityWillDissapear();

        if (shouldStartForeground && null != mService && mService.isPlaying()) {
            Intent intent = new Intent(this, LocalPlaybackService.class);
            intent.setAction(Constants.PLAYBACK_START_FOREGROUND);
            startService(intent);
        }
    }

    @Override
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        return new SubsetListLoader(this, mQueryType, mQueryCondition);
    }

    @Override
    public void onLoadFinished(Loader<List<Song>> loader, List<Song> result) {
        mAdapter.clear();

        for (Song song : result) {
            mAdapter.insert(song);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    void onServiceBound() {

    }
}