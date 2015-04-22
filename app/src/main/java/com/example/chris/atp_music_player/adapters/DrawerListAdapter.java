package com.example.chris.atp_music_player.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.DrawerItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_HEADER = 0;
    private static int TYPE_ITEM = 1;

    private ArrayList<DrawerItem> mDrawerItems;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.drawer_list_item_text)
        TextView mDrawerText;
        @InjectView(R.id.drawer_list_item_img)
        ImageView mDrawerIcon;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.inject(this, view);
        }

        public void bind(DrawerItem draweritem) {
            mDrawerText.setText(draweritem.getLabel());
            mDrawerIcon.setImageResource(draweritem.getImgId());
        }

        @Override
        public void onClick(View view) {

        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public DrawerListAdapter(ArrayList<DrawerItem> drawerItems) {
        this.mDrawerItems = drawerItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_list_header, parent, false);

            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_list, parent, false);

            return new ViewHolder(view);
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
