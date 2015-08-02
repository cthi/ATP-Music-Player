package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.ATPApplication;
import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.Genre;
import com.example.chris.atp_music_player.ui.SongGenreSubsetActivity;
import com.example.chris.atp_music_player.utils.Constants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {
    private Context mContext;
    private List<Genre> mGenreList;

    public GenreListAdapter(Context context, List<Genre> genreList) {
        mContext = context;
        mGenreList = genreList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, int position) {
        viewholder.bind(mGenreList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_genre_list_title)
        TextView mGenreTitle;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            ButterKnife.inject(this, view);
        }

        public void bind(Genre genre) {
            mGenreTitle.setText(genre.getName());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, SongGenreSubsetActivity.class);
            intent.putExtra(Constants.GENRE, mGenreList.get(getAdapterPosition()).getName());
            intent.putExtra(Constants.GENRE_ID, mGenreList.get(getAdapterPosition()).getId());
            ATPApplication.subActivityWillBeVisible();
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mGenreList.size();
    }
}

