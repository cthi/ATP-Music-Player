package com.example.chris.atp_music_player.ui

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.chris.atp_music_player.ATPApplication
import com.example.chris.atp_music_player.Constants
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.SongRequestModel
import com.example.chris.atp_music_player.adapters.SongListAdapter
import com.example.chris.atp_music_player.playback.MediaProvider
import com.example.chris.atp_music_player.playback.PlaybackService
import com.example.chris.atp_music_player.playback.SongRequest
import kotlinx.android.synthetic.main.activity_song_subset.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SongSubsetActivity : BaseServiceActivity() {
    private var mQueryType: Int = 0
    private var mQueryCondition: String = ""
    private var shouldStartForeground = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_subset)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mQueryCondition = intent.getStringExtra(Constants.QUERY_CONSTRAINT)
        mQueryType = intent.getIntExtra(Constants.QUERY_TYPE, 0)

        supportActionBar!!.title = mQueryCondition.toUpperCase()

        song_subset_recycler_view.setHasFixedSize(true)
        song_subset_recycler_view.layoutManager = LinearLayoutManager(this)

        val albumId = intent.getIntExtra(Constants.DATA_ALBUM_ID, 0)

        if (albumId != 0) {
            Glide.with(this).load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId.toLong())).fitCenter().placeholder(R.drawable.placeholder_aa).into(song_subset_img)
        } else {
            Glide.with(this).load(R.drawable.placeholder_aa).into(song_subset_img)
        }

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

    public override fun onStop() {
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
                .getSongs(mQueryType, mQueryCondition)
                .toList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ songs ->
                    song_subset_recycler_view.adapter = SongListAdapter(songs) { songs, integer ->
                        SongRequest.post(SongRequestModel(songs, integer, true))
                    }
                })
    }
}