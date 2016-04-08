package com.example.chris.atp_music_player.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.example.chris.atp_music_player.ui.AlbumListFragment
import com.example.chris.atp_music_player.ui.ArtistListFragment
import com.example.chris.atp_music_player.ui.GenreListFragment
import com.example.chris.atp_music_player.ui.SongListFragment

class LibraryPagerAdapter(val tabTitles: Array<String>, val fm: FragmentManager) :
        FragmentPagerAdapter(fm) {
    override fun getCount() = tabTitles.size

    override fun getPageTitle(position: Int) = tabTitles[position]

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ArtistListFragment()
            1 -> AlbumListFragment()
            2 -> GenreListFragment()
            3 -> SongListFragment()
            else -> ArtistListFragment()
        }
    }
}
