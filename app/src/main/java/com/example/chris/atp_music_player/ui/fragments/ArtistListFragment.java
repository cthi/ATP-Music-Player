package com.example.chris.atp_music_player.ui.fragments;


import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.ArtistListAdapter;
import com.example.chris.atp_music_player.loaders.ArtistListLoader;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArtistListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<String>> {

    private final static int LOADER = 0;

    @InjectView(R.id.artist_recycle_view)
    RecyclerView mRecyclerView;

    public ArtistListFragment() {
    }

    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new ArtistListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> result) {
        ArtistListAdapter adapter = new ArtistListAdapter(getActivity(), result);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
