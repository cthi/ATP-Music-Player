package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.ui.activities.MainActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecentSongsListAdapter extends RecyclerView.Adapter<RecentSongsListAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> mSongList;

    public RecentSongsListAdapter(Context context, List<Song> songList) {
        mContext = context;
        mSongList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.onBind(mSongList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_song_list_title)
        TextView mSongTitle;
        @InjectView(R.id.item_song_list_subtitle)
        TextView mSongArtist;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        public void onBind(Song song) {
            mSongTitle.setText(song.getTitle());
            mSongArtist.setText(song.getArtist());
        }

        @Override
        public void onClick(View view) {
            ((MainActivity) mContext).pushMediaDontQueue(mSongList, getPosition());
        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
}
