package com.example.chris.atp_music_player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.SongRequestModel
import com.example.chris.atp_music_player.adapters.SongListAdapter
import com.example.chris.atp_music_player.playback.SongRequest
import kotlinx.android.synthetic.main.no_data.*
import kotlinx.android.synthetic.main.song_list.*

class RecentSongsFragment : ATPListFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.setTitle(R.string.menu_recent)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun loadData() {
        val songList = (activity as MainActivity).service?.recentSongs?.asReversed()?.toList()

        if (songList!!.isEmpty()) {
            error.setText(R.string.err_recent)
            recycler_view.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            recycler_view.adapter = SongListAdapter(songList) { songs, integer ->
                SongRequest.post(SongRequestModel(songs, integer, true))
            }
        }
    }
}
