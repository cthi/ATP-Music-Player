package com.example.chris.atp_music_player.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.FavoritesListAdapter;
import com.example.chris.atp_music_player.models.Song;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavoritesFragment extends ATPDataFragment {

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_favs);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadData() {
        Realm realm = Realm.getInstance(getActivity());
        RealmResults<Song> favorites = realm.allObjects(Song.class);

        List<Song> songList = new ArrayList<>();

        for (Song song : favorites) {
            songList.add(song);
        }

        if (songList.isEmpty()) {
            mErrorView.setText(R.string.err_favs);
            mRecyclerView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setAdapter(new FavoritesListAdapter(getActivity(), songList));
        }
    }
}
