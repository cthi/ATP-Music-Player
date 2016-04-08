package com.example.chris.atp_music_player.ui

import android.view.View
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.SongRequestModel
import com.example.chris.atp_music_player.adapters.SongListAdapter
import com.example.chris.atp_music_player.playback.MediaProvider
import com.example.chris.atp_music_player.playback.SongRequest
import kotlinx.android.synthetic.main.no_data.*
import kotlinx.android.synthetic.main.song_list.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SongListFragment : ATPListFragment() {
    override fun loadData() {
        MediaProvider(context)
                .getAllSongs()
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ songs ->
                    if (songs.isEmpty()) {
                        error.setText(R.string.err_songs)
                        recycler_view.visibility = View.GONE
                        error.visibility = View.VISIBLE
                    } else {
                        recycler_view.adapter = SongListAdapter(songs) { songs, integer ->
                            SongRequest.post(SongRequestModel(songs, integer, true))
                        }
                    }
                })
    }
}
