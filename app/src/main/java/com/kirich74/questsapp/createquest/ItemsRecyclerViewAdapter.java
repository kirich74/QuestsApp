package com.kirich74.questsapp.createquest;

import com.kirich74.questsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import static com.kirich74.questsapp.data.ItemType.*;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class ItemsRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Quest mQuest;

    private String mName;

    private String mDescription;

    private String mMainImageUri;

    private int mAccess;

    public ItemsRecyclerViewAdapter(
            final CreateQuestActivity createQuestActivity) {

    }

    public void setQuest(String name, String description, String mainImageUri, int access,
            Quest quest) {
        mQuest = quest;
        mMainImageUri = mainImageUri;
        mName = name;
        mDescription = description;
        mAccess = access;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup,
            final int viewType) {
        View view;
        switch (viewType) {
            case TEXT:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_text, viewGroup, false);
                return new textViewHolder(view, new TextListener());
            case TEXT_ANSWER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_text_answer, viewGroup, false);
                return new textAnswerViewHolder(view, new TextAnswerListener());
            case ADD_BUTTONS:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_add_buttons, viewGroup, false);
                return new addButtonsViewHolder(view);
            case MAIN_INFO:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_main_info, viewGroup, false);
                return new mainInfoViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TEXT:
                textViewHolder textViewHolder = (textViewHolder) holder;
                textViewHolder.mTextListener.updatePosition(position);
                textViewHolder.bind(mQuest.getItem(position));
                break;
            case TEXT_ANSWER:
                textAnswerViewHolder textAnswerViewHolder = (textAnswerViewHolder) holder;
                textAnswerViewHolder.mTextAnswerListener.updatePosition(position);
                textAnswerViewHolder.bind(mQuest.getItem(position));
                break;
            case ADD_BUTTONS:
                addButtonsViewHolder addButtonsViewHolder = (addButtonsViewHolder) holder;
                addButtonsViewHolder.bind();
                break;
            case MAIN_INFO:
                mainInfoViewHolder mainInfoViewHolder = (mainInfoViewHolder) holder;
                mainInfoViewHolder.bind(mName, mDescription);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MAIN_INFO;
        }
        if (position == getItemCount() - 1) {
            return ADD_BUTTONS;
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
        //We always show first and last items (with main info and with add buttons)
        return mQuest == null ? 2
                : mQuest.size() + 2;
    }

    public class addButtonsViewHolder extends RecyclerView.ViewHolder {

        private ImageButton mAddText;

        private ImageButton mAddTextAnswer;

        addButtonsViewHolder(final View itemView) {
            super(itemView);
            mAddText = (ImageButton) itemView.findViewById(R.id.add_text_button);
            mAddTextAnswer = (ImageButton) itemView.findViewById(R.id.add_text_answer_button);
        }

        public void bind() {
            mAddText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.addTextItem("");
                    notifyDataSetChanged();
                }
            });
            mAddTextAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.addTextAnswerItem("");
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class mainInfoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageDescription;

        private Button mChoosePhotoButton;

        private EditText mTitle, mDescription;

        mainInfoViewHolder(final View itemView) {
            super(itemView);
            mImageDescription = (ImageView) itemView.findViewById(R.id.image_description_create);
            mChoosePhotoButton = (Button) itemView.findViewById(R.id.choose_photo_button);
            mTitle = (EditText) itemView.findViewById(R.id.quest_title_edit_text);
            mDescription = (EditText) itemView.findViewById(R.id.quest_description_edit_text);
        }

        public void bind(String name, String description) {
            mTitle.setText(name);
            mDescription.setText(description);
        }
    }

    public class textViewHolder extends RecyclerView.ViewHolder {

        public TextListener mTextListener;

        private EditText mEditText;

        textViewHolder(final View itemView, TextListener textListener) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_text);
            mTextListener = textListener;
            mEditText.addTextChangedListener(mTextListener);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class textAnswerViewHolder extends RecyclerView.ViewHolder {

        public TextAnswerListener mTextAnswerListener;

        private EditText mEditText;

        textAnswerViewHolder(final View itemView, TextAnswerListener textAnswerListener) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_text_answer);
            mTextAnswerListener = textAnswerListener;
            mEditText.addTextChangedListener(textAnswerListener);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_ANSWER_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class TextListener implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mQuest.editTextItem(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class TextAnswerListener implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mQuest.editTextAnswerItem(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
