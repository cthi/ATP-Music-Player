package com.example.chris.atp_music_player.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongListAdapter;
import com.example.chris.atp_music_player.models.Song;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecentSongsFragment extends Fragment {
    @InjectView(R.id.song_rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.error_placeholder)
    TextView mErrorView;

    public static RecentSongsFragment newInstance() {
        return new RecentSongsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_list, container, false);

        ButterKnife.inject(this, view);

        getActivity().setTitle(R.string.menu_recent);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Song> songList = ((MainActivity) getActivity()).getService().getRecentSongs();
        Collections.reverse(songList);

        if (songList.isEmpty()) {
            mErrorView.setText(R.string.err_recent);
            mRecyclerView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setAdapter(new SongListAdapter(getActivity(), songList));
        }

        return view;
    }
}
