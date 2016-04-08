package com.example.chris.atp_music_player.adapters

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.Song
import io.realm.Realm
import kotlinx.android.synthetic.main.item_song_list.view.*

class FavoritesListAdapter(private var songs: List<Song>, private val onClick: (List<Song>, Int) -> Unit) :
        RecyclerView.Adapter<FavoritesListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song_list, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(songs[position])
    }

    inner class ViewHolder(view: View, val onClick: (List<Song>, Int) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(song: Song) {
            with (song) {
                itemView.title.text = title
                itemView.subtitle.text = artist
                itemView.overflow.setOnClickListener { view ->
                    val popupMenu = PopupMenu(view.context, view)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.song_menu_unfav -> {
                                val realm = Realm.getInstance(view.context)
                                val songRealmResults = realm.allObjects(Song::class.java)
                                songRealmResults.removeAt(adapterPosition)
                                songs = songRealmResults.toList()
                                notifyItemRemoved(adapterPosition)
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.inflate(R.menu.song_overflow_fav_menu)
                    popupMenu.show()
                }
                itemView.setOnClickListener { onClick(songs, adapterPosition) }
            }
        }
    }

    override fun getItemCount(): Int = songs.size
}

