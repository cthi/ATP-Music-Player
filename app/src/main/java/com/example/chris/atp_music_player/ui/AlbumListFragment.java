package com.example.chris.atp_music_player.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.AlbumListAdapter;
import com.example.chris.atp_music_player.models.Album;
import com.example.chris.atp_music_player.provider.MusicProvider;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlbumListFragment extends Fragment {
    @InjectView(R.id.artist_album_rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.error_placeholder)
    TextView mErrorView;

    public static AlbumListFragment newInstance() {
        return new AlbumListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_album_list, container, false);
        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        loadAlbums();
        return view;
    }

    private void loadAlbums() {
        MusicProvider musicProvider = new MusicProvider(getActivity());
        musicProvider.getAllAlbums().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Album>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<Album> albums) {
                if (albums.isEmpty()) {
                    mErrorView.setText(R.string.err_albums);
                    mRecyclerView.setVisibility(View.GONE);
                    mErrorView.setVisibility(View.VISIBLE);
                } else {

                    mRecyclerView.setAdapter(new AlbumListAdapter(getActivity(), albums));
                }
            }
        });
    }
}
