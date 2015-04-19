package com.example.chris.atp_music_player.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.GenreListAdapter;
import com.example.chris.atp_music_player.loaders.GenreListLoader;
import com.example.chris.atp_music_player.models.Genre;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GenreListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Genre>> {

    private final static int LOADER = 225;

    @InjectView(R.id.genre_recycle_view)
    RecyclerView mRecyclerView;
    private GenreListAdapter mAdapter;

    public static GenreListFragment newInstance() {
        return new GenreListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_list, container, false);

        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new GenreListAdapter(getActivity(), new ArrayList<Genre>());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(LOADER, null, this).forceLoad();
    }

    @Override
    public Loader<List<Genre>> onCreateLoader(int id, Bundle args) {
        return new GenreListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Genre>> loader, List<Genre> result) {
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
