package com.example.chris.atp_music_player.ui

import android.content.Intent
import android.view.View
import com.example.chris.atp_music_player.ATPApplication
import com.example.chris.atp_music_player.Constants
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.adapters.GenreListAdapter
import com.example.chris.atp_music_player.playback.MediaProvider
import kotlinx.android.synthetic.main.no_data.*
import kotlinx.android.synthetic.main.song_list.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class GenreListFragment : ATPListFragment() {
    override fun loadData() {
        MediaProvider(context)
                .getAllGenres()
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ genres ->
                    if (genres.isEmpty()) {
                        recycler_view.visibility = View.GONE
                        error.setText(R.string.err_genres)
                        error.visibility = View.VISIBLE
                    } else {
                        recycler_view.adapter = GenreListAdapter(genres) { genre ->
                            val intent = Intent(context, SongGenreSubsetActivity::class.java)
                            intent.putExtra(Constants.GENRE, genre.title)
                            intent.putExtra(Constants.GENRE_ID, genre.id)
                            ATPApplication.subActivityWillBeVisible()
                            context.startActivity(intent)
                        }
                    }
                })
    }
}
