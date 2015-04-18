package com.example.chris.atp_music_player.ui.fragments;

import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongListAdapter;
import com.example.chris.atp_music_player.loaders.SongListLoader;
import com.example.chris.atp_music_player.models.Song;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Song>> {

    private final static int LOADER = 2000;

    @InjectView(R.id.song_recycle_view)
    RecyclerView mRecyclerView;

    public SongListFragment() {
    }

    public static SongListFragment newInstance() {
        return new SongListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
    }

    @Override
    public Loader<List<Song>> onCreateLoader(int id, Bundle args) {
        return new SongListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Song>> loader, List<Song> result) {

        SongListAdapter adapter = new SongListAdapter(getActivity(), result);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
