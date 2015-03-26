package com.example.chris.atp_music_player.ui.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.DrawerListAdapter;
import com.example.chris.atp_music_player.loaders.MusicQueryLoader;
import com.example.chris.atp_music_player.models.DrawerItem;
import com.example.chris.atp_music_player.services.LocalPlaybackService;
import com.example.chris.atp_music_player.ui.fragments.LibraryFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Boolean>  {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LocalPlaybackService mService;
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    @InjectView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawerList) RecyclerView mDrawerList;

    private boolean mBound;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            LocalPlaybackService.LocalBinder binder = (LocalPlaybackService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
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

        Loader loader = getSupportLoaderManager().initLoader(0, null, this);
        loader.forceLoad();

        Intent intent = new Intent(this, LocalPlaybackService.class);
        startService(intent);
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

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
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

    public void pushMedia(Uri uri) {
        mService.play(uri);
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
}
