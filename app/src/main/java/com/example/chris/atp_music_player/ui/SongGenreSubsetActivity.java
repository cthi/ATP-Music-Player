package com.example.chris.atp_music_player.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.chris.atp_music_player.ATPApplication;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongSubsetListAdapter;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.provider.MusicProvider;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SongGenreSubsetActivity extends BaseServiceActivity {
    @InjectView(R.id.song_genre_subset_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.song_genre_subset_recycle_view)
    RecyclerView mRecyclerView;

    private boolean shouldStartForeground = true;

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
        musicProvider.getSongsWithGenre(getIntent().getIntExtra(Constants.GENRE_ID, 0)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Song>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Song> songs) {
                mRecyclerView.setAdapter(new SongSubsetListAdapter(SongGenreSubsetActivity.this,
                        songs));
            }
        });
    }
}