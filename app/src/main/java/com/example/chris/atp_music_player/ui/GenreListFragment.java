package com.example.chris.atp_music_player.ui;

import android.view.View;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.GenreListAdapter;
import com.example.chris.atp_music_player.models.Genre;
import com.example.chris.atp_music_player.provider.MusicProvider;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GenreListFragment extends ATPDataFragment {

    public static GenreListFragment newInstance() {
        return new GenreListFragment();
    }

    @Override
    protected void loadData() {
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
