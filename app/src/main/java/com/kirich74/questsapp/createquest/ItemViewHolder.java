package com.kirich74.questsapp.createquest;

import org.json.JSONObject;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Kirill Pilipenko on 07.07.2017.
 */

public interface ItemViewHolder {
    public void bind (JSONObject item);
}
