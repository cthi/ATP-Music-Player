package com.example.chris.atp_music_player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.Song
import com.example.chris.atp_music_player.SongRequestModel
import com.example.chris.atp_music_player.adapters.FavoritesListAdapter
import com.example.chris.atp_music_player.playback.SongRequest
import io.realm.Realm
import kotlinx.android.synthetic.main.no_data.*
import kotlinx.android.synthetic.main.song_list.*

class FavoritesFragment : ATPListFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.setTitle(R.string.menu_favs)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun loadData() {
        val favorites = Realm.getInstance(context).allObjects(Song::class.java)
        val songList = favorites.map { song -> song }

        if (songList.isEmpty()) {
            error.setText(R.string.err_favs)
            recycler_view.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            recycler_view.adapter = FavoritesListAdapter(songList, { songs, position ->
                SongRequest.post(SongRequestModel(songs, position, true))
            })
        }
    }
}
