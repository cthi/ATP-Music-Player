package com.example.chris.atp_music_player.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.chris.atp_music_player.R
import com.example.chris.atp_music_player.adapters.LibraryPagerAdapter

import kotlinx.android.synthetic.main.fragment_library.*

class LibraryFragment : Fragment() {
    private val PAGE_NUM = "page_num"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_library, container, false)
        activity.setTitle(R.string.menu_lib)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        library_viewpager.adapter = LibraryPagerAdapter(resources.getStringArray(R.array.library_pager_items),
                childFragmentManager)
        library_viewpager.currentItem = if (savedInstanceState != null) savedInstanceState.getInt(PAGE_NUM) else 0
        library_tabs.setupWithViewPager(library_viewpager)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt(PAGE_NUM, library_viewpager.currentItem)
    }
}
