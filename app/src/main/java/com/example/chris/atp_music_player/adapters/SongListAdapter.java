package com.example.chris.atp_music_player.adapters;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongListAdapter extends CursorRecyclerAdapter<SongListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_song_list_title) TextView mTitle;
        @InjectView(R.id.item_song_list_subtitle) TextView mSubtitle;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.inject(this, view);
        }

        public void bind(Cursor cursor) {
            mTitle.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            mSubtitle.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        }

        @Override
        public void onClick(View view) {

        }
    }

    public SongListAdapter(Cursor cursor) {
        super(cursor);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder viewHolder, Cursor cursor){
        viewHolder.bind(cursor);
    }
}
