package com.example.chris.atp_music_player.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.chris.atp_music_player.ATPApplication
import com.example.chris.atp_music_player.Constants
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.SongRequestModel
import com.example.chris.atp_music_player.adapters.SongListAdapter
import com.example.chris.atp_music_player.playback.MediaProvider
import com.example.chris.atp_music_player.playback.PlaybackService
import com.example.chris.atp_music_player.playback.SongRequest
import kotlinx.android.synthetic.main.activity_song_genre_subset.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SongGenreSubsetActivity : BaseServiceActivity() {
    private var shouldStartForeground = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_genre_subset)

        setSupportActionBar(song_genre_subset_toolbar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = intent.getStringExtra(Constants.GENRE)

        song_genre_subset_recycle_view.setHasFixedSize(true)
        song_genre_subset_recycle_view.layoutManager = LinearLayoutManager(this)

        loadSongs()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        shouldStartForeground = false
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        ATPApplication.subActivityWillDissapear()

        if (shouldStartForeground && null != mService && mService.isPlaying) {
            val intent = Intent(this, PlaybackService::class.java)
            intent.action = Constants.PLAYBACK_START_FOREGROUND
            startService(intent)
        }
    }

    private fun loadSongs() {
        MediaProvider(this)
                .getSongsWithGenre(intent.getIntExtra(Constants.GENRE_ID, 0))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ songs ->
                    song_genre_subset_recycle_view.adapter = SongListAdapter(songs) { songs, integer ->
                        SongRequest.post(SongRequestModel(songs, integer, true))
                    }
                })
    }
}