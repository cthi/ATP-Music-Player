package com.example.chris.atp_music_player.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.chris.atp_music_player.Album
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.albumArtUri
import kotlinx.android.synthetic.main.item_artist_list.view.*

class AlbumListAdapter(val albums: List<Album>, val onClick: (Album) -> Unit) :
        RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListAdapter.ViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist_list, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: AlbumListAdapter.ViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount() = albums.size

    class ViewHolder(view: View, val onClick: (Album) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(album: Album) {
            with(album) {
                itemView.item_artist_list_label.text = title
                itemView.setOnClickListener { onClick(this) }

                Glide.with(itemView.context)
                        .load(albumArtUri())
                        .placeholder(R.drawable.placeholder_aa)
                        .into(itemView.item_artist_list_img)
            }
        }
    }
}
