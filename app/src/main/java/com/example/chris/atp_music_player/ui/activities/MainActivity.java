package com.example.chris.atp_music_player.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.DrawerListAdapter;
import com.example.chris.atp_music_player.models.DrawerItem;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.ui.fragments.LibraryFragment;
import com.example.chris.atp_music_player.ui.fragments.RecentSongsFragment;
import com.example.chris.atp_music_player.ui.widgets.NowPlayingWidget;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.ResourceUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseServiceActivity implements DrawerListAdapter.ViewHolder.DrawerListClick {

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawerList)
    RecyclerView mDrawerList;

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
        DrawerListAdapter adapter = new DrawerListAdapter(this, test);
        //
        mDrawerList.setAdapter(adapter);

        setSupportActionBar(mToolbar);
        initDrawerLayout();

        LibraryFragment fragment = LibraryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
        getSupportActionBar().setTitle("Music Library");

        Intent intent = new Intent(this, LocalPlaybackService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restorePlayingView();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, 0); // this disables the animation
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
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

    @Override
    public void onItemClick(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment;

                if (position == 1) {
                    fragment = LibraryFragment.newInstance();
                    setTitle("Music Library");
                } else if (position == 2) {
                    fragment = RecentSongsFragment.newInstance();
                    setTitle("Favorites");
                } else {
                    fragment = RecentSongsFragment.newInstance();
                    setTitle("Recently Played");
                }

                Fragment current = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
                if (!current.getClass().equals(fragment.getClass())) {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    ft.replace(R.id.main_fragment, fragment).commit();
                }
            }
        }, 350);

        mDrawerLayout.closeDrawers();
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
    }
}