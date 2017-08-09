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

import static com.kirich74.questsapp.data.ItemType.ADD_BUTTONS;
import static com.kirich74.questsapp.data.ItemType.DESCRIPTION;
import static com.kirich74.questsapp.data.ItemType.MAIN_INFO;
import static com.kirich74.questsapp.data.ItemType.NAME;
import static com.kirich74.questsapp.data.ItemType.TEXT;
import static com.kirich74.questsapp.data.ItemType.TEXT_;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER_;
import static com.kirich74.questsapp.data.ItemType.TYPE;
import static com.kirich74.questsapp.data.ItemType.UNKNOWN_TYPE;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class ItemsRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Quest mQuest;

    public ItemsRecyclerViewAdapter(
            final CreateQuestActivity createQuestActivity) {

    }

    public void setQuest(/*String name, String description, String mainImageUri, int access,*/
            Quest quest) {
        mQuest = quest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup,
            final int viewType) {
        View view;
        switch (viewType) {
            case TEXT:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_text, viewGroup, false);
                return new textViewHolder(view, new EditableTextListener());
            case TEXT_ANSWER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_text_answer, viewGroup, false);
                return new textAnswerViewHolder(view, new EditableTextListener());
            case ADD_BUTTONS:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_add_buttons, viewGroup, false);
                return new addButtonsViewHolder(view);
            case MAIN_INFO:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_main_info, viewGroup, false);
                return new mainInfoViewHolder(view, new EditableTextListener(),
                        new EditableTextListener());
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TEXT:
                textViewHolder textViewHolder = (textViewHolder) holder;
                textViewHolder.mTextListener.updatePosition(position, TEXT);
                textViewHolder.bind(mQuest.getItem(position));
                break;
            case TEXT_ANSWER:
                textAnswerViewHolder textAnswerViewHolder = (textAnswerViewHolder) holder;
                textAnswerViewHolder.mTextAnswerListener.updatePosition(position, TEXT_ANSWER);
                textAnswerViewHolder.bind(mQuest.getItem(position));
                break;
            case ADD_BUTTONS:
                addButtonsViewHolder addButtonsViewHolder = (addButtonsViewHolder) holder;
                addButtonsViewHolder.bind();
                break;
            case MAIN_INFO:
                mainInfoViewHolder mainInfoViewHolder = (mainInfoViewHolder) holder;
                mainInfoViewHolder.mNameListener.updatePosition(position, NAME);
                mainInfoViewHolder.mDescriptionListener.updatePosition(position, DESCRIPTION);
                mainInfoViewHolder.bind(mQuest.mName, mQuest.mDescription);
                break;
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
        return mQuest == null ? 0
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
                    mQuest.addTextItem();
                    notifyDataSetChanged();
                }
            });
            mAddTextAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.addTextAnswerItem();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class mainInfoViewHolder extends RecyclerView.ViewHolder {

        public EditableTextListener mNameListener, mDescriptionListener;

        private ImageView mImageDescription;

        private Button mChoosePhotoButton;

        private EditText mName, mDescription;

        mainInfoViewHolder(final View itemView, EditableTextListener nameListener,
                EditableTextListener descriptionListener) {
            super(itemView);
            mImageDescription = (ImageView) itemView.findViewById(R.id.image_description_create);
            mChoosePhotoButton = (Button) itemView.findViewById(R.id.choose_photo_button);
            mName = (EditText) itemView.findViewById(R.id.quest_title_edit_text);
            mDescription = (EditText) itemView.findViewById(R.id.quest_description_edit_text);
            mNameListener = nameListener;
            mDescriptionListener = descriptionListener;
            mName.addTextChangedListener(mNameListener);
            mDescription.addTextChangedListener(mDescriptionListener);
        }

        public void bind(String name, String description) {
            mName.setText(name);
            mDescription.setText(description);
            //TODO picture, button
        }
    }

    public class textViewHolder extends RecyclerView.ViewHolder {

        public EditableTextListener mTextListener;

        private EditText mEditText;

        textViewHolder(final View itemView, EditableTextListener listener) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_create_text);
            mTextListener = listener;
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

        public EditableTextListener mTextAnswerListener;

        private EditText mEditText;

        textAnswerViewHolder(final View itemView, EditableTextListener listener) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_create_text_answer);
            mTextAnswerListener = listener;
            mEditText.addTextChangedListener(listener);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_ANSWER_));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class EditableTextListener implements TextWatcher {

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
    }
}
