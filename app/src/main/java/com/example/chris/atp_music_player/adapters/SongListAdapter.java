package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Song;
import com.example.chris.atp_music_player.ui.MainActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> mSongList;

    public SongListAdapter(Context context, List<Song> songList) {
        mContext = context;
        mSongList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mOverflow.setOnClickListener(new OnSongOverflowMenuClicked(viewHolder, mSongList, mContext));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(mSongList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_song_list_title)
        TextView mTitle;
        @InjectView(R.id.item_song_list_subtitle)
        TextView mSubtitle;
        @InjectView(R.id.item_song_overflow)
        ImageButton mOverflow;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            ButterKnife.inject(this, view);
        }

        public void bind(Song song) {
            mTitle.setText(song.getTitle());
            mSubtitle.setText(song.getArtist());
        }

        @Override
        public void onClick(View view) {
            ((MainActivity) mContext).pushMedia(mSongList, getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
}
