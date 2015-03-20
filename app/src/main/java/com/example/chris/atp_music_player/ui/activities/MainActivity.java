package com.example.chris.atp_music_player.ui.activities;

import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.DrawerListAdapter;
import com.example.chris.atp_music_player.models.DrawerItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    @InjectView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawerList) RecyclerView mDrawerList;

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

    public void initDrawerLayout(){
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
    }
}
