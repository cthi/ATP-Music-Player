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
import io.realm.RealmResults;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> mSongList;

    public FavoritesListAdapter(Context context, List<Song> songList) {
        mContext = context;
        mSongList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mOverflow.setOnClickListener(new OnSongOverflowMenuClicked(viewHolder));

        return viewHolder;
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
        @InjectView(R.id.item_song_overflow)
        ImageButton mOverflow;

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
            ((MainActivity) mContext).pushMediaDontQueue(mSongList, getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    private class OnSongOverflowMenuClicked implements View.OnClickListener, PopupMenu
            .OnMenuItemClickListener {
        RecyclerView.ViewHolder viewHolder;

        public OnSongOverflowMenuClicked(RecyclerView.ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(mContext, v);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.song_overflow_fav_menu);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.song_menu_unfav:
                    Realm realm = Realm.getInstance(mContext);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<Song> songRealmResults = realm.allObjects(Song.class);
                            songRealmResults.remove(viewHolder.getAdapterPosition());

                            mSongList.remove(viewHolder.getAdapterPosition());
                            notifyItemRemoved(viewHolder.getAdapterPosition());
                        }
                    });
                    return true;
                default:
                    return false;
            }
        }
    }
}

