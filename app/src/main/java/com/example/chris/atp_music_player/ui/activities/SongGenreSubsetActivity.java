package com.example.chris.atp_music_player.ui.activities;

import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongSubsetListAdapter;
import com.example.chris.atp_music_player.loaders.GenreSubsetListLoader;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongGenreSubsetActivity extends BaseServiceActivity
        implements LoaderManager.LoaderCallbacks<List<Song>> {

    private int LOADER = 57;

    @InjectView(R.id.song_genre_subset_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.song_genre_subset_recycle_view)
    RecyclerView mRecyclerView;

    private SongSubsetListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_genre_subset);

        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.GENRE));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new SongSubsetListAdapter(this, new ArrayList<Song>());
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
    }

    @Override
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        return new GenreSubsetListLoader(this, getIntent().getIntExtra(Constants.GENRE_ID, 0));
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