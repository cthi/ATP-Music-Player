package com.example.chris.atp_music_player.adapters;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.ui.activities.SongSubsetActivity;
import com.example.chris.atp_music_player.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArtistListAdapter extends CursorRecyclerAdapter<ArtistListAdapter.ViewHolder>  {

    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_artist_list_label) TextView mLabel;

        public ViewHolder(View view){
            super(view);

            view.setOnClickListener(this);
            ButterKnife.inject(this, view);
        }

        public void bind(Cursor cursor) {
            mLabel.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, SongSubsetActivity.class);
            intent.putExtra(Constants.QUERY_CONSTRAINT, mLabel.getText().toString());
            intent.putExtra(Constants.QUERY_TYPE, Constants.QUERY_TYPE_ARTIST);

            mContext.startActivity(intent);
        }
    }

    public ArtistListAdapter(Cursor cursor, Context context) {
        super(cursor);

        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder viewholder, Cursor cursor){
        viewholder.bind(cursor);
    }
}
