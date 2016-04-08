package com.example.chris.atp_music_player.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chris.atp_music_player.Genre
import com.example.chris.atp_music_player.R
import kotlinx.android.synthetic.main.item_genre_list.view.*

class GenreListAdapter(val genres: List<Genre>, val onClick: (Genre) -> Unit) :
        RecyclerView.Adapter<GenreListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreListAdapter.ViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre_list, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: GenreListAdapter.ViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount() = genres.size

    class ViewHolder(view: View, val onClick: (Genre) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(genre: Genre) {
            with(genre) {
                itemView.item_genre_list_title.text = title
                itemView.setOnClickListener { onClick(this) }
            }
        }
    }
}

