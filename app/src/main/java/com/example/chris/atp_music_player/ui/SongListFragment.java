package com.example.chris.atp_music_player.ui;

import android.view.View;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongListAdapter;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.provider.MusicProvider;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SongListFragment extends ATPDataFragment {

    public static SongListFragment newInstance() {
        return new SongListFragment();
    }

    @Override
    protected void loadData() {
        MusicProvider musicProvider = new MusicProvider(getActivity());
        musicProvider.getAllSongs().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Song>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<Song> songs) {
                if (songs.isEmpty()) {
                    mErrorView.setText(R.string.err_songs);
                    mRecyclerView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setAdapter(new SongListAdapter(getActivity(), songs));
                }
            }
        });
    }

}
