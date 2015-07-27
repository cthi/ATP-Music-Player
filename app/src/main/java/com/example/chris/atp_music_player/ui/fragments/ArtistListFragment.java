package com.example.chris.atp_music_player.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.ArtistListAdapter;
import com.example.chris.atp_music_player.models.Artist;
import com.example.chris.atp_music_player.provider.MusicProvider;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ArtistListFragment extends Fragment {
    @InjectView(R.id.artist_recycle_view)
    RecyclerView mRecyclerView;

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

        loadArtists();
        return view;
    }

    private void loadArtists() {
        MusicProvider musicProvider = new MusicProvider(getActivity());
        musicProvider.getAllArtists().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Artist>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Artist> artists) {
                mRecyclerView.setAdapter(new ArtistListAdapter(getActivity(), artists));
            }
        });
    }
}
