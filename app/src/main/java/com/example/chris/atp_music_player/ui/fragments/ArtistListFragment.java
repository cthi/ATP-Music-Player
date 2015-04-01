package com.example.chris.atp_music_player.ui.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.ArtistListAdapter;
import com.example.chris.atp_music_player.db.MusicLibraryDbContract;
import com.example.chris.atp_music_player.db.MusicLibraryDbHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArtistListFragment extends Fragment {
    @InjectView(R.id.artist_recycle_view) RecyclerView recyclerView;

    public ArtistListFragment() {
    }

    public static ArtistListFragment newInstance(){
        return new ArtistListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);

        ButterKnife.inject(this, view);


        SQLiteDatabase db = new MusicLibraryDbHelper(getActivity()).getReadableDatabase();
        String query = "SELECT DISTINCT id _id, artist FROM " + MusicLibraryDbContract.MusicLibraryEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);
        ArtistListAdapter adapter = new ArtistListAdapter(cursor, getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
