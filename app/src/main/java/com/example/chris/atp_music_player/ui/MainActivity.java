package com.example.chris.atp_music_player.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.chris.atp_music_player.ATPApplication;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.ui.widgets.NowPlayingWidget;
import com.example.chris.atp_music_player.utils.Constants;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseServiceActivity {
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer)
    NavigationView mNavView;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingPanel;
    @InjectView(R.id.now_playing)
    NowPlayingWidget mNowPlayingWidget;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initToolbar();
        initNavView();
        linkDrawer();

        if (null == savedInstanceState) {
            LibraryFragment fragment = LibraryFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, LocalPlaybackService.class);
        if (mService.isPlaying()) {
            if (ATPApplication.willSubActivityBeVisible()) {
                intent.setAction(Constants.PLAYBACK_STOP_FOREGROUND);
            } else {
                intent.setAction(Constants.PLAYBACK_START_FOREGROUND);
            }

            startService(intent);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSlidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mSlidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void pushMedia(List<Song> songList, int position) {
        super.pushMedia(songList, position);

        mNowPlayingWidget.updateNowPlayingView(songList.get(position));
    }

    @Override
    public void pushMediaDontQueue(List<Song> songList, int position) {
        super.pushMediaDontQueue(songList, position);

        mNowPlayingWidget.updateNowPlayingView(songList.get(position));
    }

    public void restorePlayingView() {
        if (mServiceBound && mService.getLastSong() != null) {
            mNowPlayingWidget.updateNowPlayingView(mService.getLastSong());
        }
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initNavView() {
        mNavView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                Fragment switchTo = null;

                switch (menuItem.getItemId()) {
                    case R.id.menu_library:
                        switchTo = LibraryFragment.newInstance();
                        break;
                    case R.id.menu_fav:
                        switchTo = FavoritesFragment.newInstance();
                        break;
                    case R.id.menu_recent:
                        switchTo = RecentSongsFragment.newInstance();
                        break;
                }

                menuItem.setChecked(true);

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id
                        .main_fragment);
                final Fragment tempFragment = switchTo;

                if (switchTo.getClass() != currentFragment.getClass()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            ft.replace(R.id.main_fragment, tempFragment).commit();
                        }
                    }, 300);
                }

                return true;
            }
        });
    }

    private void linkDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public LocalPlaybackService getService() {
        if (mServiceBound) {
            return mService;
        }
        return null;
    }

    @Override
    void onServiceBound() {
        mNowPlayingWidget.setService(mService);
        restorePlayingView();
    }
}
