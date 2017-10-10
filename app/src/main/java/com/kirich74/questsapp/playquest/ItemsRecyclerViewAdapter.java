package com.kirich74.questsapp.playquest;

/**
 * Created by Kirill Pilipenko on 10.10.2017.
 */

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.Quest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.kirich74.questsapp.data.ItemType.DESCRIPTION;
import static com.kirich74.questsapp.data.ItemType.IMAGE;
import static com.kirich74.questsapp.data.ItemType.MAIN_INFO;
import static com.kirich74.questsapp.data.ItemType.NAME;
import static com.kirich74.questsapp.data.ItemType.TEXT;
import static com.kirich74.questsapp.data.ItemType.TEXT_;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER_;
import static com.kirich74.questsapp.data.ItemType.TYPE;
import static com.kirich74.questsapp.data.ItemType.UNKNOWN_TYPE;

public class ItemsRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Quest mQuest;

    private Context mContext;

    private int viewWidth;

    ItemsRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setQuest(Quest quest) {
        mQuest = quest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup,
            final int viewType) {
        final View view;
        switch (viewType) {
            case TEXT:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_text, viewGroup, false);
                return new com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.textViewHolder(
                        view);
            case TEXT_ANSWER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_text_answer, viewGroup, false);
                return new com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.textAnswerViewHolder(
                        view);
            case MAIN_INFO:
                WindowManager windowManager = (WindowManager) mContext
                        .getSystemService(Context.WINDOW_SERVICE);
                viewWidth = windowManager.getDefaultDisplay().getWidth();
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_main_info, viewGroup, false);
                return new com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.mainInfoViewHolder(
                        view);
            case IMAGE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_image, viewGroup, false);
                return new com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.ImageViewHolder(
                        view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TEXT:
                com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.textViewHolder
                        textViewHolder
                        = (com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.textViewHolder) holder;
                textViewHolder.bind(mQuest.getItem(position));
                break;
            case TEXT_ANSWER:
                com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.textAnswerViewHolder
                        textAnswerViewHolder
                        = (com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.textAnswerViewHolder) holder;
                textAnswerViewHolder.bind(mQuest.getItem(position));
                break;
            case MAIN_INFO:
                com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.mainInfoViewHolder
                        mainInfoViewHolder
                        = (com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.mainInfoViewHolder) holder;
                mainInfoViewHolder.bind(mQuest.mName, mQuest.mDescription);
                break;
            case IMAGE:
                com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.ImageViewHolder
                        imageViewHolder
                        = (com.kirich74.questsapp.playquest.ItemsRecyclerViewAdapter.ImageViewHolder) holder;
                imageViewHolder.bind();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MAIN_INFO;
        }
        try {
            return mQuest.getItem(position).getInt(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return UNKNOWN_TYPE;
    }


    @Override
    public int getItemCount() {
        //We always show first item (with main info)
        return mQuest == null ? 0
                : mQuest.size();
    }

    public class mainInfoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageDescription;

        private Button mStartButton;

        private TextView mName, mDescription;

        mainInfoViewHolder(final View itemView) {
            super(itemView);
            mImageDescription = (ImageView) itemView.findViewById(R.id.image_description_play);
            mStartButton = (Button) itemView.findViewById(R.id.start_button);
            mName = (TextView) itemView.findViewById(R.id.quest_title_text_view);
            mDescription = (TextView) itemView.findViewById(R.id.quest_description_text_view);
        }

        public void bind(String name, String description) {
            mName.setText(name);
            mDescription.setText(description);
            mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //TODO
                }
            });

            if (!mQuest.mMainImageUri.isEmpty()) {
                Picasso.with(mContext)
                        .load(mQuest.mMainImageUri).resize(viewWidth, viewWidth).centerCrop()
                        .into(mImageDescription);
            }

        }
    }

    public class textViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        textViewHolder(final View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_play_text);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mTextView.setText(item.getString(TEXT_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class textAnswerViewHolder extends RecyclerView.ViewHolder {

        private EditText mEditText;

        private Button mCheckAnswerButton;

        textAnswerViewHolder(final View itemView) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_play_text_answer);
            mCheckAnswerButton = (Button) itemView
                    .findViewById(R.id.item_play_check_text_answer_button);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_ANSWER_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCheckAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    try {
                        if (mEditText.getText().toString() == item.getString(TEXT_ANSWER_)) {
                            Log.d("Check", "correct");
                            //TODO
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        ImageViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_play_image);
        }

        public void bind() {
            Uri imagePath = mQuest.getImageUri(getAdapterPosition());
            if (!imagePath.toString().isEmpty()) {
                Picasso.with(mContext)
                        .load(mQuest.getImageUri(getAdapterPosition())).resize(viewWidth, viewWidth)
                        .centerCrop().into(mImageView);
            }
        }
    }

    /*private class EditableTextListener implements TextWatcher {

        int type;

        private int position;

        public void updatePosition(int position, int type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            switch (type) {
                case TEXT:
                    mQuest.editTextItem(position, charSequence.toString());
                    break;
                case TEXT_ANSWER:
                    mQuest.editTextAnswerItem(position, charSequence.toString());
                    break;
                case NAME:
                    mQuest.mName = charSequence.toString();
                    break;
                case DESCRIPTION:
                    mQuest.mDescription = charSequence.toString();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }*/
}

