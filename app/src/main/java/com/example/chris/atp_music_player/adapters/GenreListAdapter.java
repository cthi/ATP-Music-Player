package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mGenreList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.item_genre_list_title)
        TextView mGenreTitle;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            ButterKnife.inject(this, view);
        }

        public void bind(String genre) {
            mGenreTitle.setText(genre);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public GenreListAdapter(Context context, List<String> genreList) {
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

    @Override
    public int getItemCount() {
        return mGenreList.size();
    }

    public void clear() {
        mGenreList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void insert(String genre) {
        mGenreList.add(genre);
    }
}
