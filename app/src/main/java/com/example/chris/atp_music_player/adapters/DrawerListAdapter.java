package com.example.chris.atp_music_player.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.atp_music_player.R;
import com.example.chris.atp_music_player.models.DrawerItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {
    private ArrayList<DrawerItem> mDrawerItems;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.drawer_list_item_text) TextView mTextView;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.inject(this, view);
        }

        public void bind(DrawerItem draweritem) {
            mTextView.setText(draweritem.getLabel());
        }

        @Override
        public void onClick(View view) {

        }
    }

    public DrawerListAdapter(ArrayList<DrawerItem> drawerItems){
        this.mDrawerItems = drawerItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos){
        holder.bind(mDrawerItems.get(pos));
    }

    @Override
    public int getItemCount(){
        return mDrawerItems.size();
    }
}
