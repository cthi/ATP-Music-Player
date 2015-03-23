package com.example.chris.atp_music_player.ui.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.adapters.SongListAdapter;
import com.example.chris.atp_music_player.db.MusicLibraryDbContract;
import com.example.chris.atp_music_player.db.MusicLibraryDbHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongListFragment extends Fragment {
    @InjectView(R.id.song_recycle_view) RecyclerView recyclerView;

    public SongListFragment() {
        // Required empty public constructor
    }

    public static SongListFragment newInstance() {
        return new SongListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        ButterKnife.inject(this,view);


        SQLiteDatabase db = new MusicLibraryDbHelper(getActivity()).getReadableDatabase();
        String query = "SELECT id _id, * FROM " + MusicLibraryDbContract.MusicLibraryEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);
        SongListAdapter adapter = new SongListAdapter(cursor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

}
