package com.example.chris.atp_music_player.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import com.example.chris.atp_music_player.ATPApplication
import com.example.chris.atp_music_player.Constants
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.Song
import com.example.chris.atp_music_player.playback.PlaybackService
import com.example.chris.atp_music_player.playback.SongRequest
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.now_playing_view.*

class MainActivity : BaseServiceActivity() {
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initNavView()
        linkDrawer()

        if (null == savedInstanceState) {
            val fragment = LibraryFragment()
            supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit()
        }

        SongRequest.observable().subscribe({ songRequestModel ->
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        })
    }

    override fun onStop() {
        super.onStop()
        val intent = Intent(this, PlaybackService::class.java)
        if (mService.isPlaying) {
            if (ATPApplication.willSubActivityBeVisible()) {
                intent.action = Constants.PLAYBACK_STOP_FOREGROUND
            } else {
                intent.action = Constants.PLAYBACK_START_FOREGROUND
            }

            startService(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        if (mServiceBound && mService.lastSong != null && sliding_layout.panelState == SlidingUpPanelLayout.PanelState.HIDDEN) {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)

        if (null != supportActionBar) {
            supportActionBar!!.title = title
        }
    }

    override fun onBackPressed() {
        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle!!.onConfigurationChanged(newConfig)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initNavView() {
        drawer.setNavigationItemSelectedListener { menuItem ->
            drawer_layout.closeDrawers()
            menuItem.isChecked = true

            val switchTo = when (menuItem.itemId) {
                R.id.menu_library -> LibraryFragment()
                R.id.menu_fav -> FavoritesFragment()
                R.id.menu_recent -> RecentSongsFragment()
                else -> LibraryFragment()
            }
            val currentFragment = supportFragmentManager.findFragmentById(R.id.main_fragment)

            if (switchTo.javaClass != currentFragment.javaClass) {
                Handler().postDelayed({
                    val ft = supportFragmentManager.beginTransaction()
                    ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    ft.replace(R.id.main_fragment, switchTo).commit()
                }, 300)
            }

            true
        }
    }

    fun restorePlayingView() {
        if (mServiceBound && mService.lastSong != null) {
            now_playing.updateNowPlayingView(mService.lastSong as Song)
        } else {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        }
    }

    private fun linkDrawer() {
        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar as Toolbar, R.string.drawer_open, R.string.drawer_close)
        (drawerToggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
    }

    val service: PlaybackService?
        get() {
            if (mServiceBound) {
                return mService
            }
            return null
        }

    internal override fun onServiceBound() {
        now_playing.setService(mService)
        restorePlayingView()
    }
}
