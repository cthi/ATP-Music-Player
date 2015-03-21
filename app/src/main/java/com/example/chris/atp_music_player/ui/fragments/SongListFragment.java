package com.example.chris.atp_music_player.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends Fragment {


    public SongListFragment() {
        // Required empty public constructor
    }

    public static SongListFragment newInstance() {
        return new SongListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }


}
