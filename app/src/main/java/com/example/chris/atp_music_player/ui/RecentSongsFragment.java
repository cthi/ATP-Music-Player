package com.example.chris.atp_music_player.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongListAdapter;
import com.example.chris.atp_music_player.models.Song;

import java.util.Collections;
import java.util.List;

public class RecentSongsFragment extends ATPDataFragment {

    public static RecentSongsFragment newInstance() {
        return new RecentSongsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_recent);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadData() {
        List<Song> songList = ((MainActivity) getActivity()).getService().getRecentSongs();
        Collections.reverse(songList);

        if (songList.isEmpty()) {
            mErrorView.setText(R.string.err_recent);
            mRecyclerView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setAdapter(new SongListAdapter(getActivity(), songList));
        }
    }
}
