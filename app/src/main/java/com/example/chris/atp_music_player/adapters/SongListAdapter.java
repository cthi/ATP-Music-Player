package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.ui.activities.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SongListAdapter extends CursorRecyclerAdapter<SongListAdapter.ViewHolder> {

    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_song_list_title) TextView mTitle;
        @InjectView(R.id.item_song_list_subtitle) TextView mSubtitle;
        String media_uri;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            ButterKnife.inject(this, view);
        }

        public void bind(Cursor cursor) {
            mTitle.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            mSubtitle.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            media_uri = cursor.getString(cursor.getColumnIndex("data"));
        }

        @Override
        public void onClick(View view) {
            ((MainActivity) mContext).pushMedia(mTitle.getText().toString(), mSubtitle.getText().toString(), Uri.parse(media_uri));
        }
    }

    public SongListAdapter(Cursor cursor, Context context) {
        super(cursor);

        mContext = context;
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
