package com.kirich74.questsapp.createquest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ItemsViewHolder> {

    private Quest mQuest;

    @Override
    public ItemsViewHolder onCreateViewHolder(final ViewGroup parent,
            final int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final ItemsViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        public ItemsViewHolder(final View itemView) {
            super(itemView);
        }
    }
}
