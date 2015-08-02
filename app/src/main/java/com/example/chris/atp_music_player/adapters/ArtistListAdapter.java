package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.atp_music_player.ATPApplication;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Artist;
import com.example.chris.atp_music_player.ui.SongSubsetActivity;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> {
    private Context mContext;
    private List<Artist> mArtistList;

    public ArtistListAdapter(Context context, List<Artist> artistList) {
        mContext = context;
        mArtistList = artistList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, int position) {
        viewholder.bind(mArtistList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_artist_list_label)
        TextView mLabel;
        @InjectView(R.id.item_artist_list_img)
        ImageView mImage;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            ButterKnife.inject(this, view);
        }

        public void bind(Artist artist) {
            mLabel.setText(artist.getName());

            Glide.with(mContext)
                    .load(AlbumArtUtils.albumArtUriFromId(artist.getAlbumID()))
                    .placeholder(R.drawable.placeholder_aa)
                    .into(mImage);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, SongSubsetActivity.class);
            intent.putExtra(Constants.QUERY_CONSTRAINT, mArtistList.get(getAdapterPosition()).getName());
            intent.putExtra(Constants.QUERY_TYPE, Constants.QUERY_TYPE_ARTIST);
            intent.putExtra(Constants.DATA_ALBUM_ID, mArtistList.get(getAdapterPosition()).getAlbumID());
            ATPApplication.subActivityWillBeVisible();
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }
}
