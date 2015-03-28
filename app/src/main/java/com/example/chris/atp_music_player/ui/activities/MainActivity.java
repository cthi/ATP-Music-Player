package com.example.chris.atp_music_player.ui.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.example.chris.atp_music_player.loaders.MusicQueryLoader;
import com.example.chris.atp_music_player.models.DrawerItem;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.receivers.ReceiverMessages;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.ui.fragments.LibraryFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Boolean> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LocalPlaybackService mService;
    private BroadcastReceiver mServiceReceiver;

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    @InjectView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawerList) RecyclerView mDrawerList;

    // move this
    @InjectView(R.id.sliding_layout_song_title) TextView mTitle;
    @InjectView(R.id.sliding_layout_song_artist) TextView mArtist;
    @InjectView(R.id.sliding_layout_action_ico) ImageView mActionImage;
    @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mSlidingPanel;

    private boolean mServiceBound;
    private boolean mReceiverRegistered;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            LocalPlaybackService.LocalBinder binder = (LocalPlaybackService.LocalBinder) service;
            mService = binder.getService();
            mServiceBound = true;
            restorePlayingView();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDrawerList.setLayoutManager(new LinearLayoutManager(this));
        // test
        DrawerItem a = new DrawerItem("Library");
        DrawerItem b = new DrawerItem("Favorites");
        DrawerItem c = new DrawerItem("Recently Added");
        DrawerItem d = new DrawerItem("TEST");
        DrawerItem e = new DrawerItem("TEST");

        ArrayList<DrawerItem> test  = new ArrayList<DrawerItem>();
        test.add(a);
        test.add(b);
        test.add(c);
        test.add(d);
        test.add(e);
        DrawerListAdapter adapter = new DrawerListAdapter(test);
        //
        mDrawerList.setAdapter(adapter);

        setSupportActionBar(mToolbar);
        initDrawerLayout();

        LibraryFragment fragment = LibraryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment).commit();

      //  Loader loader = getSupportLoaderManager().initLoader(0, null, this);
        //loader.forceLoad();

        Intent intent = new Intent(this, LocalPlaybackService.class);
        startService(intent);

        mSlidingPanel.setEnableDragViewTouchEvents(true);
        mSlidingPanel.setDragView(findViewById(R.id.drag_view));

        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mReceiverRegistered) {
            mServiceReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int result = intent.getIntExtra(ReceiverMessages.PLAYBACK_INTENT_TAG, 0);

                    if (result == ReceiverMessages.PLAYBACK_STOPPED) {
                        // update gui
                    } else if (result == ReceiverMessages.STREAM_ENDED) {
                        // send a new song to the service
                    }
                }
            };

            registerReceiver(mServiceReceiver, new IntentFilter("android.intent.action.MAIN"));
            mReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        if (mReceiverRegistered) {
            unregisterReceiver(mServiceReceiver);
            mReceiverRegistered = false;
        }
    }

    public void initListeners(){
        mActionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (mService.isPlaying()) {
                    mService.pause();
                    mActionImage.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                } else {
                    mService.resume();
                    mActionImage.setImageResource(R.drawable.ic_pause_white_24dp);
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
    public void setTitle(CharSequence title){
        super.setTitle(title);
        mToolbar.setTitle(title);
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

    public void initDrawerLayout(){
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
    }

    public void pushMedia(String title, String artist, Uri uri) {
        mService.play(title, artist, uri);

        mArtist.setText(artist);
        mTitle.setText(title);
        mActionImage.setImageResource(R.drawable.ic_pause_white_24dp);
    }
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new MusicQueryLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean result) {
        Log.d(TAG, "DONE");
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    public void restorePlayingView(){
        if (mServiceBound) {
            if (mService.isPlaying()) {
                mActionImage.setImageResource(R.drawable.ic_pause_white_24dp);
            } else {
                mActionImage.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            }

            Song song = mService.getLastSong();

            mArtist.setText(song.getArtist());
            mTitle.setText(song.getTitle());
        }
    }
}
