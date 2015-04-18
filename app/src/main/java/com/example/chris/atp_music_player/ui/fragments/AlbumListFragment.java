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
import com.example.chris.atp_music_player.adapters.AlbumListAdapter;
import com.example.chris.atp_music_player.loaders.AlbumListLoader;
import com.example.chris.atp_music_player.models.Album;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AlbumListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Album>> {

    private final static int LOADER = 1000;

    @InjectView(R.id.artist_recycle_view)
    RecyclerView mRecyclerView;
    private AlbumListAdapter mAdapter;

    public AlbumListFragment() {
    }

    public static AlbumListFragment newInstance() {
        return new AlbumListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mAdapter = new AlbumListAdapter(getActivity(), new ArrayList<Album>());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
    }

    @Override
    public Loader<List<Album>> onCreateLoader(int id, Bundle args) {
        return new AlbumListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Album>> loader, List<Album> result) {
        mAdapter.clear();

        for (int i = 0; i < result.size(); i++) {
            mAdapter.insert(result.get(i));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}