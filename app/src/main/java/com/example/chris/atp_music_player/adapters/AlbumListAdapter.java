package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Album;
import com.example.chris.atp_music_player.transform.PaletteTransformation;
import com.example.chris.atp_music_player.ui.activities.SongSubsetActivity;
import com.example.chris.atp_music_player.utils.AlbumArtUtils;
import com.example.chris.atp_music_player.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolder> {

    private Context mContext;
    private List<Album> mAlbumList;

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

        public void bind(Album album) {
            mLabel.setText(album.getTitle());

            final PaletteTransformation paletteTransformation = PaletteTransformation.instance();

            Picasso.with(mContext)
                    .load(AlbumArtUtils.albumArtUriFromId(album.getId()))
                    .placeholder(R.drawable.placeholder_aa)
                    .transform(paletteTransformation)
                    .into(mImage, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bmp = ((BitmapDrawable) mImage.getDrawable()).getBitmap();
                            Palette palette = PaletteTransformation.getPalette(bmp);

                            if (palette.getLightMutedSwatch() != null) {
                                mLabel.setBackgroundColor(palette.getLightMutedSwatch().getRgb());
                            } else {
                                mLabel.setBackgroundResource(R.color.muted_blue);
                            }
                        }
                    });
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, SongSubsetActivity.class);
            intent.putExtra(Constants.QUERY_CONSTRAINT, mAlbumList.get(getPosition()).getTitle());
            intent.putExtra(Constants.QUERY_TYPE, Constants.QUERY_TYPE_ALBUM);
            intent.putExtra(Constants.DATA_ALBUM_ID, mAlbumList.get(getPosition()).getId());

            mContext.startActivity(intent);
        }
    }

    public AlbumListAdapter(Context context, List<Album> albumList) {

        mContext = context;
        mAlbumList = albumList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, int position) {
        viewholder.bind(mAlbumList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    public void clear() {
        mAlbumList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void insert(Album album) {
        mAlbumList.add(album);
    }
}