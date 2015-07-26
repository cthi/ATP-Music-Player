package com.example.chris.atp_music_player.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.FavoritesListAdapter;
import com.example.chris.atp_music_player.adapters.RecentSongsListAdapter;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class FavoritesFragment extends Fragment {
    @InjectView(R.id.favorites_rv)
    RecyclerView mRecyclerView;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.inject(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getFavorites();
        return view;
    }

    private void getFavorites() {
        Realm realm = Realm.getInstance(getActivity());
        RealmResults<Song> favorites = realm.allObjects(Song.class);

        List<Song> songList = new ArrayList<>();

        for (Song song : favorites) {
            songList.add(song);
        }

        mRecyclerView.setAdapter(new FavoritesListAdapter(getActivity(), songList));
    }
}
