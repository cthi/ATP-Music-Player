package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.ui.fragments.AlbumListFragment;
import com.example.chris.atp_music_player.ui.fragments.ArtistListFragment;
import com.example.chris.atp_music_player.ui.fragments.GenreListFragment;
import com.example.chris.atp_music_player.ui.fragments.SongListFragment;

public class LibraryPagerAdapter extends FragmentPagerAdapter {

    private final String[] TAB_TITLES;

    public LibraryPagerAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        TAB_TITLES = context.getResources().getStringArray(R.array.library_pager_items);
    }

    @Override
    public Fragment getItem(int position){
        switch(position) {
            case 0:
                return ArtistListFragment.newInstance();
            case 1:
                return AlbumListFragment.newInstance();
            case 2:
                return GenreListFragment.newInstance();
            case 3:
                return SongListFragment.newInstance();
            default:
                return ArtistListFragment.newInstance();
        }
    }

    @Override
    public int getCount(){
        return TAB_TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return TAB_TITLES[position];
    }
}