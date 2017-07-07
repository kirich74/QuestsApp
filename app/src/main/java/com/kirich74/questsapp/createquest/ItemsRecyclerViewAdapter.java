package com.kirich74.questsapp.createquest;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.ItemType;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import static com.kirich74.questsapp.data.ItemType.*;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class ItemsRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Quest mQuest;

    public void setQuest(Quest quest){
        mQuest = quest;
    }

    public ItemsRecyclerViewAdapter(
            final CreateQuestActivity createQuestActivity) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        View view;
        switch (viewType) {
            case TEXT:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_text, viewGroup, false);
                return new textViewHolder(view);
            case TEXT_ANSWER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_text_answer, viewGroup, false);
                return new textAnswerViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TEXT:
                textViewHolder textViewHolder = (textViewHolder) holder;
                textViewHolder.bind(mQuest.getItem(position));
                break;
            case TEXT_ANSWER:
                textAnswerViewHolder textAnswerViewHolder = (textAnswerViewHolder) holder;
                textAnswerViewHolder.bind(mQuest.getItem(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        try {
            return mQuest.getItem(position).getInt(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return UNKNOWN_TYPE;
    }


    @Override
    public int getItemCount() {
        return mQuest.size();
    }

    public class textViewHolder extends RecyclerView.ViewHolder implements ItemViewHolder {
        private EditText mEditText;
        textViewHolder(final View itemView) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_text);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class textAnswerViewHolder extends RecyclerView.ViewHolder implements ItemViewHolder {
        private EditText mEditText;
        textAnswerViewHolder(final View itemView) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_text_answer);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_ANSWER_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
