package com.kirich74.questsapp.createquest;

import com.kirich74.questsapp.FirstLaunch.PrefManager;
import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.ImageUtils;
import com.kirich74.questsapp.data.Quest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import static com.kirich74.questsapp.data.ItemType.ADD_BUTTONS;
import static com.kirich74.questsapp.data.ItemType.DESCRIPTION;
import static com.kirich74.questsapp.data.ItemType.IMAGE;
import static com.kirich74.questsapp.data.ItemType.MAIN_INFO;
import static com.kirich74.questsapp.data.ItemType.NAME;
import static com.kirich74.questsapp.data.ItemType.NEXT_STEP;
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

    public int selectedStep;

    private Quest mQuest;

    private onItemActionListener mOnItemActionListener;

    private Context mContext;

    private int viewWidth;

    ItemsRecyclerViewAdapter(onItemActionListener onItemActionListener, Context context) {
        mOnItemActionListener = onItemActionListener;
        mContext = context;
    }

    public void setQuest(/*String name, String description, String mainImageUri, int access,*/
            Quest quest) {
        mQuest = quest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup,
            final int viewType) {
        final View view;
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
                WindowManager windowManager = (WindowManager) mContext
                        .getSystemService(Context.WINDOW_SERVICE);
                viewWidth = windowManager.getDefaultDisplay().getWidth();
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_main_info, viewGroup, false);
                return new mainInfoViewHolder(view, new EditableTextListener(),
                        new EditableTextListener());
            case IMAGE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_create_image, viewGroup, false);
                return new ImageViewHolder(view);
            case NEXT_STEP:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_next_step, viewGroup, false);
                return new NextStepViewHolder(view);
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
                mainInfoViewHolder.bind(mQuest.mName, mQuest.mDescription, mQuest.mAccess);
                break;
            case IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.bind(mQuest.getItem(position));
                break;
            case NEXT_STEP:
                NextStepViewHolder nextStepViewHolder = (NextStepViewHolder) holder;
                nextStepViewHolder.bind(mQuest.getItem(position));
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

    public void removeItem(int position) {

        notifyItemRemoved(position);
    }

    public void saveImage(Bitmap bitmap) {
        PrefManager prefManager = new PrefManager(mContext);
        Uri mUri = ImageUtils
                .saveBitmapToFile(mContext, bitmap, mQuest.getGlobalId(), selectedStep);
        if (selectedStep == 0) {
            mQuest.mMainImageUri = mUri.toString();
        } else {
            mQuest.editImageItem(selectedStep, mUri != null ? mUri.toString() : null);
        }
        notifyDataSetChanged();
    }

    public void setGlobalId(final int globalId) {
        mQuest.setGlobalId(globalId);
    }

    public class addButtonsViewHolder extends RecyclerView.ViewHolder {

        private Button mAddText, mAddTextAnswer, mAddImage, mAddNext;

        addButtonsViewHolder(final View itemView) {
            super(itemView);
            mAddText = (Button) itemView.findViewById(R.id.add_text_button);
            mAddTextAnswer = (Button) itemView.findViewById(R.id.add_text_answer_button);
            mAddImage = (Button) itemView.findViewById(R.id.add_image_button);
            mAddNext = (Button) itemView.findViewById(R.id.add_simple_answer_button);
        }

        public void bind() {
            mAddText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.addTextItem();
                    notifyItemInserted(getAdapterPosition());
                }
            });
            mAddTextAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.addTextAnswerItem();
                    notifyItemInserted(getAdapterPosition());
                }
            });
            mAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.addImageItem();
                    notifyItemInserted(getAdapterPosition());
                }
            });
            mAddNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    mQuest.addNextItem();
                    notifyItemInserted(getAdapterPosition());
                }
            });
        }
    }

    public class mainInfoViewHolder extends RecyclerView.ViewHolder {

        public EditableTextListener mNameListener, mDescriptionListener;

        private ImageView mImageDescription;

        private Button mChoosePhotoButton;

        private EditText mName, mDescription;

        private CheckBox mAvailableForAllCheckBox;

        mainInfoViewHolder(final View itemView, EditableTextListener nameListener,
                EditableTextListener descriptionListener) {
            super(itemView);
            mImageDescription = (ImageView) itemView.findViewById(R.id.image_description_create);
            mChoosePhotoButton = (Button) itemView.findViewById(R.id.choose_photo_button);
            mName = (EditText) itemView.findViewById(R.id.quest_title_create_text);
            mDescription = (EditText) itemView.findViewById(R.id.quest_description_create_text);
            mAvailableForAllCheckBox = (CheckBox) itemView
                    .findViewById(R.id.quest_available_for_all_checkbox);
            mNameListener = nameListener;
            mDescriptionListener = descriptionListener;
            mName.addTextChangedListener(mNameListener);
            mDescription.addTextChangedListener(mDescriptionListener);
        }

        public void bind(String name, String description, int access) {
            mName.setText(name);
            mDescription.setText(description);
            mChoosePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    selectedStep = getAdapterPosition();
                    mOnItemActionListener.setImage();
                }
            });

            if (!mQuest.mMainImageUri.isEmpty()) {
                Picasso.with(mContext)
                        .load(mQuest.mMainImageUri).resize(viewWidth, viewWidth).centerCrop()
                        .into(mImageDescription);
            }
            if (access == 1) {
                mAvailableForAllCheckBox.setChecked(true);
            } else {
                mAvailableForAllCheckBox.setChecked(false);
            }
            mAvailableForAllCheckBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(final CompoundButton buttonView,
                                final boolean isChecked) {
                            if (isChecked) {
                                mQuest.mAccess = 1;
                            } else {
                                mQuest.mAccess = 0;
                            }
                        }
                    });
        }
    }

    public class NextStepViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton mCancelButton;

        private Button mNextStepButton;

        NextStepViewHolder(final View itemView) {
            super(itemView);
            mNextStepButton = itemView.findViewById(R.id.item_next_step_button);
            mCancelButton = (ImageButton) itemView.findViewById(R.id.cancel_action);
        }

        public void bind(@NonNull final JSONObject item) {

            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.deleteItem(item);
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }

    public class textViewHolder extends RecyclerView.ViewHolder {

        public EditableTextListener mTextListener;

        private EditText mEditText;

        private ImageButton mCancelButton;

        textViewHolder(final View itemView, EditableTextListener listener) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_create_text);
            mTextListener = listener;
            mEditText.addTextChangedListener(mTextListener);
            mCancelButton = (ImageButton) itemView.findViewById(R.id.cancel_action);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_));
                if (getAdapterPosition() == getItemCount() - 2 && mEditText.getText().length() == 0){
                    mEditText.requestFocus();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.deleteItem(item);
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }

    public class textAnswerViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton mCancelButton;

        public EditableTextListener mTextAnswerListener;

        private EditText mEditText;

        textAnswerViewHolder(final View itemView, EditableTextListener listener) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_create_text_answer);
            mTextAnswerListener = listener;
            mEditText.addTextChangedListener(listener);
            mCancelButton = (ImageButton) itemView.findViewById(R.id.cancel_action);
        }

        public void bind(@NonNull final JSONObject item) {
            try {
                mEditText.setText(item.getString(TEXT_ANSWER_));
                if (getAdapterPosition() == getItemCount() - 2 && mEditText.getText().length() == 0){
                    mEditText.requestFocus();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.deleteItem(item);
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton mCancelButton;

        private ImageView mImageView;

        private Button mSetImageButton;

        ImageViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_create_image);
            mSetImageButton = (Button) itemView.findViewById(R.id.item_create_choose_photo_button);
            mCancelButton = (ImageButton) itemView.findViewById(R.id.cancel_action);
        }

        public void bind(@NonNull final JSONObject item) {
            mSetImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    selectedStep = getAdapterPosition();
                    mOnItemActionListener.setImage();
                }
            });
            if (getAdapterPosition() == getItemCount() - 2){
                mSetImageButton.requestFocus();
            }

            String imagePath = mQuest.getImageUri(getAdapterPosition()).toString();

            if (!imagePath.isEmpty()) {
                Picasso.with(mContext)
                        .load(imagePath).resize(viewWidth, viewWidth)
                        .centerCrop().into(mImageView);
            }
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mQuest.deleteItem(item);
                    notifyItemRemoved(getAdapterPosition());
                }
            });
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
