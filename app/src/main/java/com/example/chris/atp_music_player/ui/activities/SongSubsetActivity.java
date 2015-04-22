package com.example.chris.atp_music_player.ui.activities;

import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongSubsetListAdapter;
import com.example.chris.atp_music_player.loaders.SubsetListLoader;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.Constants;
import com.squareup.picasso.Picasso;

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
            Picasso.with(this).load(AlbumArtUtils.albumArtUriFromId(albumId))
                    .placeholder(R.drawable.placeholder_aa).into(mAlbumImage);
        } else {
            Picasso.with(this).load(R.drawable.placeholder_aa).into(mAlbumImage);
        }

        getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
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
}