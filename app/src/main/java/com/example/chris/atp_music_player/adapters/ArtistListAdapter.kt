package com.example.chris.atp_music_player.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.chris.atp_music_player.Artist
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.albumArtUri
import kotlinx.android.synthetic.main.item_artist_list.view.*

class ArtistListAdapter(val artists: List<Artist>, val onClick: (Artist) -> Unit) :
        RecyclerView.Adapter<ArtistListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistListAdapter.ViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist_list, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ArtistListAdapter.ViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    override fun getItemCount() = artists.size

    class ViewHolder(view: View, val onClick: (Artist) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(artist: Artist) {
            with(artist) {
                itemView.item_artist_list_label.text = name
                itemView.setOnClickListener { onClick(this) }

                Glide.with(itemView.context).load(albumArtUri())
                        .placeholder(R.drawable.placeholder_aa)
                        .into(itemView.item_artist_list_img)
            }
        }
    }
}
