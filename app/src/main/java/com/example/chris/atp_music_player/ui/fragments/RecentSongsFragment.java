package com.example.chris.atp_music_player.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.RecentSongsListAdapter;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.ui.activities.MainActivity;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecentSongsFragment extends Fragment {

    @InjectView(R.id.recent_songs_recycle_view)
    RecyclerView mRecyclerView;

    public RecentSongsFragment() {

    }

    public static RecentSongsFragment newInstance() {
        return new RecentSongsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_songs, container, false);

        ButterKnife.inject(this, view);

        getActivity().setTitle(R.string.menu_recent);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Song> songList = ((MainActivity) getActivity()).getService().getRecentSongs();
        Collections.reverse(songList);
        RecentSongsListAdapter adapter = new RecentSongsListAdapter(getActivity(), songList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }
}
