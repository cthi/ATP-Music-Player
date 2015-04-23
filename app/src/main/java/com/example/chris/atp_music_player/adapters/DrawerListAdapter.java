package com.example.chris.atp_music_player.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.DrawerItem;
import com.example.chris.atp_music_player.ui.activities.MainActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_HEADER = 0;
    private static int TYPE_ITEM = 1;

    private Context mContext;
    private ArrayList<DrawerItem> mDrawerItems;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.drawer_list_item_text)
        TextView mDrawerText;
        @InjectView(R.id.drawer_list_item_img)
        ImageView mDrawerIcon;

        DrawerListClick mCallback;

        public static interface DrawerListClick {
            public void onItemClick(int position);
        }

        public ViewHolder(View view, DrawerListClick listener) {
            super(view);

            ButterKnife.inject(this, view);

            view.setOnClickListener(this);
            mCallback = listener;
        }

        public void bind(DrawerItem draweritem) {
            mDrawerText.setText(draweritem.getLabel());
            mDrawerIcon.setImageResource(draweritem.getImgId());
        }

        @Override
        public void onClick(View view) {
            mCallback.onItemClick(getPosition());
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public DrawerListAdapter(Context context, ArrayList<DrawerItem> drawerItems) {
        this.mContext = context;
        this.mDrawerItems = drawerItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_list_header, parent, false);

            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_list, parent, false);

            return new ViewHolder(view, (MainActivity) mContext);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(mDrawerItems.get(pos));
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }

    }
}
