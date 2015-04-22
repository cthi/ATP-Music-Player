package com.example.chris.atp_music_player.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.DrawerListAdapter;
import com.example.chris.atp_music_player.models.DrawerItem;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.receivers.ReceiverMessages;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.ui.fragments.LibraryFragment;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.ResourceUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseServiceActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BroadcastReceiver mServiceReceiver;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawerList)
    RecyclerView mDrawerList;

    // move this
    @InjectView(R.id.sliding_layout_song_title)
    TextView mTitle;
    @InjectView(R.id.sliding_layout_song_artist)
    TextView mArtist;
    @InjectView(R.id.sliding_layout_action_ico)
    ImageView mActionImage;
    @InjectView(R.id.sliding_layout_top_artist)
    TextView mTopArtist;
    @InjectView(R.id.sliding_layout_top_title)
    TextView mTopTitle;
    @InjectView(R.id.sliding_layout_top_album)
    TextView mTopAlbum;
    @InjectView(R.id.sliding_layout_top_song_img)
    ImageView mTopImage;

    @InjectView(R.id.sliding_layout_top_repeat)
    ImageView mTopRepeat;
    @InjectView(R.id.sliding_layout_top_back)
    ImageView mTopBack;
    @InjectView(R.id.sliding_layout_top_fwrd)
    ImageView mTopForward;
    @InjectView(R.id.sliding_layout_top_play_pause)
    ImageView mTopPlayPause;
    @InjectView(R.id.sliding_layout_top_shuffle)
    ImageView mTopShuffle;

    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingPanel;

    private boolean mShuffle = false;
    private boolean mRepeat = false;

    private boolean mReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDrawerList.setLayoutManager(new LinearLayoutManager(this));
        // test
        DrawerItem header = new DrawerItem("Header");
        DrawerItem a = new DrawerItem("Library",
                ResourceUtils.getDrawableResourceId("ic_lib_24dp", this));
        DrawerItem b = new DrawerItem("Favorites",
                ResourceUtils.getDrawableResourceId("ic_favorites_24dp", this));
        DrawerItem c = new DrawerItem("Recently Played",
                ResourceUtils.getDrawableResourceId("ic_playlist_24dp", this));

        ArrayList<DrawerItem> test = new ArrayList<>();
        test.add(header);
        test.add(a);
        test.add(b);
        test.add(c);
        DrawerListAdapter adapter = new DrawerListAdapter(test);
        //
        mDrawerList.setAdapter(adapter);

        setSupportActionBar(mToolbar);
        initDrawerLayout();

        LibraryFragment fragment = LibraryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();

        Intent intent = new Intent(this, LocalPlaybackService.class);
        startService(intent);

        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restorePlayingView();

        if (!mReceiverRegistered) {
            mServiceReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int result = intent.getIntExtra(ReceiverMessages.PLAYBACK_INTENT_TAG, 0);

                    if (result == ReceiverMessages.PLAYBACK_STOPPED) {

                    } else if (result == ReceiverMessages.STREAM_ENDED) {

                        if (mRepeat) {
                            mService.repeat();
                        } else if (mShuffle) {
                            mService.playRandom();
                        }
                       
                        updateNowPlayingView(mService.getLastSong());
                    }
                }
            };

            registerReceiver(mServiceReceiver, new IntentFilter("android.intent.action.MAIN"));
            mReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mReceiverRegistered) {
            unregisterReceiver(mServiceReceiver);
            mReceiverRegistered = false;
        }
    }

    public void initListeners() {
        mActionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService.isPlaying()) {
                    mService.pause();
                    mActionImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                } else {
                    mService.resume();
                    mActionImage.setImageResource(R.drawable.ic_pause_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
                }
            }
        });

        mTopPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService.isPlaying()) {
                    mService.pause();
                    mActionImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                } else {
                    mService.resume();
                    mActionImage.setImageResource(R.drawable.ic_pause_white_36dp);
                    mTopPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
                }
            }
        });

        mTopForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mShuffle) {
                    mService.playRandom();
                } else {
                    mService.playNext();
                }

                updateNowPlayingView(mService.getLastSong());
            }
        });

        mTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mShuffle) {
                    mService.playRandom();
                } else {
                    mService.playLast();
                }

                updateNowPlayingView(mService.getLastSong());
            }
        });

        mTopShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShuffle = !mShuffle;
                if (mShuffle) {
                    mTopShuffle.setImageResource(R.drawable.ic_shuffle_cyan_36dp);
                } else {
                    mTopShuffle.setImageResource(R.drawable.ic_shuffle_white_36dp);
                }

            }
        });

        mTopRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRepeat = !mRepeat;

                if (mRepeat) {
                    mTopRepeat.setImageResource(R.drawable.ic_replay_cyan_36dp);
                } else {
                    mTopRepeat.setImageResource(R.drawable.ic_replay_white_36dp);
                }
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mToolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (mSlidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public void initDrawerLayout() {
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.dark_blue));
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
    }

    @Override
    public void pushMedia(List<Song> songList, int position) {
        super.pushMedia(songList, position);

        updateNowPlayingView(songList.get(position));
    }

    public void updateNowPlayingView(Song song) {
        mArtist.setText(song.getArtist());
        mTitle.setText(song.getTitle());
        mTopArtist.setText(song.getArtist());
        mTopTitle.setText(song.getTitle());
        mTopAlbum.setText(song.getAlbum());

        if (mService.isPlaying()) {
            mActionImage.setImageResource(R.drawable.ic_pause_white_36dp);
            mTopPlayPause.setImageResource(R.drawable.ic_pause_white_36dp);
        } else {
            mActionImage.setImageResource(R.drawable.ic_play_arrow_white_36dp);
            mTopPlayPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
        }

        Picasso.with(this).load(AlbumArtUtils.albumArtUriFromId(song.getAlbumId())).into(mTopImage);
    }

    public void restorePlayingView() {

        if (mServiceBound && mService.getLastSong() != null) {
            updateNowPlayingView(mService.getLastSong());
        }
    }
}