package com.example.chris.atp_music_player.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.GenreListAdapter;
import com.example.chris.atp_music_player.models.Genre;
import com.example.chris.atp_music_player.provider.MusicProvider;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GenreListFragment extends Fragment {
    @InjectView(R.id.song_rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.error_placeholder)
    TextView mErrorView;

    public static GenreListFragment newInstance() {
        return new GenreListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_list, container, false);
        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadGenres();
        return view;
    }

    private void loadGenres() {
        MusicProvider musicProvider = new MusicProvider(getActivity());
        musicProvider.getAllGenres().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Genre>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Genre> genres) {
                if (genres.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    mErrorView.setText(R.string.err_genres);
                    mErrorView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setAdapter(new GenreListAdapter(getActivity(), genres));
                }
            }
        });
    }
}
