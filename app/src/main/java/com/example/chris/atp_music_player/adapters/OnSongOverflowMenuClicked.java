package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Song;

import java.util.List;

import io.realm.Realm;

public class OnSongOverflowMenuClicked implements View.OnClickListener, PopupMenu
        .OnMenuItemClickListener {
    private Context mContext;
    private RecyclerView.ViewHolder viewHolder;
    private List<Song> mSongList;

    public OnSongOverflowMenuClicked(RecyclerView.ViewHolder viewHolder, List<Song> songlist, Context context) {
        this.viewHolder = viewHolder;
        this.mSongList = songlist;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.song_overflow_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.song_menu_fav:
                Realm realm = Realm.getInstance(mContext);

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(mSongList.get(viewHolder.getAdapterPosition()));
                    }
                });
                return true;
            default:
                return false;
        }
    }
}