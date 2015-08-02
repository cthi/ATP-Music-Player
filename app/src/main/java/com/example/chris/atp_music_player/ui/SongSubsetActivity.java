package com.example.chris.atp_music_player.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.chris.atp_music_player.ATPApplication;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongListAdapter;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.provider.MusicProvider;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SongSubsetActivity extends BaseServiceActivity {

    private int mQueryType;
    private String mQueryCondition;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.song_subset_recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.song_subset_img)
    ImageView mAlbumImage;

    private boolean shouldStartForeground = true;

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

        int albumId = getIntent().getIntExtra(Constants.DATA_ALBUM_ID, 0);

        if (albumId != 0) {
            Glide.with(this).load(AlbumArtUtils.albumArtUriFromId(albumId))
                    .fitCenter()
                    .placeholder(R.drawable.placeholder_aa).into(mAlbumImage);
        } else {
            Glide.with(this).
                    load(R.drawable.placeholder_aa).into(mAlbumImage);
        }

        loadSongs();
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
    public void onStop() {
        super.onStop();
        ATPApplication.subActivityWillDissapear();

        if (shouldStartForeground && null != mService && mService.isPlaying()) {
            Intent intent = new Intent(this, LocalPlaybackService.class);
            intent.setAction(Constants.PLAYBACK_START_FOREGROUND);
            startService(intent);
        }
    }

    private void loadSongs() {
        MusicProvider musicProvider = new MusicProvider(this);
        musicProvider.getSongs(mQueryType, mQueryCondition).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Song>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<Song> songs) {
                mRecyclerView.setAdapter(new SongListAdapter(SongSubsetActivity.this, songs));
            }
        });
    }
}