package com.example.chris.atp_music_player.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.Song
import kotlinx.android.synthetic.main.item_song_list.view.*

class SongListAdapter(val songs: List<Song>, val onClick: (List<Song>, Int) -> Unit) :
        RecyclerView.Adapter<SongListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListAdapter.ViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song_list, parent, false)
        return ViewHolder(view, songs, onClick)
    }

    override fun onBindViewHolder(holder: SongListAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount() = songs.size

    class ViewHolder(view: View, val songs: List<Song>, val onClick: (List<Song>, Int) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(song: Song) {
            with(song) {
                itemView.title.text = title
                itemView.subtitle.text = artist
                itemView.setOnClickListener { onClick(songs, adapterPosition) }
            }
        }
    }
}