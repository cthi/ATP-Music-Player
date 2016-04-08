package com.example.chris.atp_music_player.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chris.atp_music_player.ATPApplication
import com.example.chris.atp_music_player.Constants
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.adapters.AlbumListAdapter
import com.example.chris.atp_music_player.playback.MediaProvider
import kotlinx.android.synthetic.main.artist_album_list.*
import kotlinx.android.synthetic.main.no_data.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class AlbumListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.artist_album_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        artist_album_rv.setHasFixedSize(true)
        artist_album_rv.layoutManager = GridLayoutManager(context, 2)

        loadAlbums()
    }

    private fun loadAlbums() {
        MediaProvider(context)
                .getAllAlbums()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ albums ->
                    if (albums.isEmpty()) {
                        showErrorView()
                    } else {
                        artist_album_rv.adapter = AlbumListAdapter(albums, { album ->
                            val intent = Intent(context, SongSubsetActivity::class.java)
                            intent.putExtra(Constants.QUERY_CONSTRAINT, album.title)
                            intent.putExtra(Constants.QUERY_TYPE, Constants.QUERY_TYPE_ALBUM)
                            intent.putExtra(Constants.DATA_ALBUM_ID, album.id)
                            ATPApplication.subActivityWillBeVisible()
                            context.startActivity(intent)
                        })
                    }
                }, {
                    showErrorView()
                })
    }

    private fun showErrorView() {
        error.setText(R.string.err_albums)
        error.visibility = View.VISIBLE
        artist_album_rv.visibility = View.GONE
    }
}
