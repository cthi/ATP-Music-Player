package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.ui.activities.SongSubsetActivity;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder>  {

    private Context mContext;
    private List<String> mArtistList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_artist_list_label) TextView mLabel;
        @InjectView(R.id.item_artist_list_img) ImageView mImage;

        public ViewHolder(View view){
            super(view);

            view.setOnClickListener(this);
            ButterKnife.inject(this, view);
        }

        public void bind(String label) {
            mLabel.setText(label);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, SongSubsetActivity.class);
            intent.putExtra(Constants.QUERY_CONSTRAINT, mArtistList.get(getPosition()));
            intent.putExtra(Constants.QUERY_TYPE, Constants.QUERY_TYPE_ARTIST);
            intent.putExtra(Constants.DATA_ALBUM_ID, 0);
            mContext.startActivity(intent);
        }
    }

    public ArtistListAdapter(Context context, List<String> artistList) {

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
    public void onBindViewHolder(ViewHolder viewholder, int position){
        viewholder.bind(mArtistList.get(position));
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }
}