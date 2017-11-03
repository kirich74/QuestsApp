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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import static com.kirich74.questsapp.data.ItemType.ANSWER;
import static com.kirich74.questsapp.data.ItemType.IMAGE;
import static com.kirich74.questsapp.data.ItemType.IMAGE_;
import static com.kirich74.questsapp.data.ItemType.MAIN_INFO;
import static com.kirich74.questsapp.data.ItemType.NEXT_STEP;
import static com.kirich74.questsapp.data.ItemType.QUEST_FINISHED;
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

    private int begin, end;

    private com.kirich74.questsapp.playquest.onItemActionListener mOnItemActionListener;

    ItemsRecyclerViewAdapter(Context context, onItemActionListener onItemActionListener) {
        mContext = context;
        mOnItemActionListener = onItemActionListener;
    }

    public void setQuest(Quest quest) {
        mQuest = quest;
        begin = 0;
        end = 0;
    }

    public void nextStep() {
        begin = end + 1;
        if (begin > mQuest.size()) {
            end = begin;
            notifyDataSetChanged();
            return;
        } else {
            for (int i = begin; i < mQuest.size(); i++) {
                try {
                    if (mQuest.getItem(i).getInt(TYPE) > ANSWER) {
                        end = i;
                        notifyDataSetChanged();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        end = mQuest.size();
        notifyDataSetChanged();
    }

    public void previousStep() {
        end = begin - 1;
        if (end < 0) {
            return;
        } else {
            for (int i = end; i > 0; i--) {
                try {
                    if (mQuest.getItem(i).getInt(TYPE) > ANSWER) {
                        begin = i + 1;
                        notifyDataSetChanged();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        begin = 0;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup,
            final int viewType) {
        final View view;
        switch (viewType) {
            case TEXT:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_text, viewGroup, false);
                return new textViewHolder(view);
            case TEXT_ANSWER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_text_answer, viewGroup, false);
                return new textAnswerViewHolder(view);
            case MAIN_INFO:
                WindowManager windowManager = (WindowManager) mContext
                        .getSystemService(Context.WINDOW_SERVICE);
                viewWidth = windowManager.getDefaultDisplay().getWidth();
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_main_info, viewGroup, false);
                return new mainInfoViewHolder(view);
            case IMAGE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_image, viewGroup, false);
                return new ImageViewHolder(view);
            case QUEST_FINISHED:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_play_finish, viewGroup, false);
                return new FinishViewHolder(view);
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
                textViewHolder.bind(mQuest.getItem(getPositionInQuest(position)));
                break;
            case TEXT_ANSWER:
                textAnswerViewHolder textAnswerViewHolder= (textAnswerViewHolder) holder;
                textAnswerViewHolder.bind(mQuest.getItem(getPositionInQuest(position)));
                break;
            case MAIN_INFO:
                mainInfoViewHolder mainInfoViewHolder = (mainInfoViewHolder) holder;
                mainInfoViewHolder.bind(mQuest.mName, mQuest.mDescription);
                break;
            case IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.bind(mQuest.getItem(getPositionInQuest(position)));
                break;
            case NEXT_STEP:
                NextStepViewHolder nextStepViewHolder = (NextStepViewHolder) holder;
                nextStepViewHolder.bind();
                break;
            case QUEST_FINISHED:
                FinishViewHolder finishViewHolder = (FinishViewHolder) holder;
                finishViewHolder.bind();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        position = getPositionInQuest(position);
        if (position == 0) {
            return MAIN_INFO;
        }
        if (position == mQuest.size() + 1) {
            return QUEST_FINISHED;
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
        //and when quest is finished we show button that it finished
        try {
            return mQuest == null ? 0
                    : (end == mQuest.size() && mQuest.getItem(end).getInt(TYPE) < ANSWER) ? end - begin + 2
                            : end - begin + 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPositionInQuest(int position) {
        return position + begin;
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
                    nextStep();
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
            mEditText.setText("");
            mCheckAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    try {
                        if (Objects.equals(mEditText.getText().toString(),
                                item.getString(TEXT_ANSWER_))) {
                            Log.d("Check", "correct");
                            nextStep();
                        } else {
                            mOnItemActionListener.wrongAnswerToast();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public class FinishViewHolder extends RecyclerView.ViewHolder {

        private Button mFinishButton;

        FinishViewHolder(final View itemView) {
            super(itemView);
            mFinishButton = (Button) itemView
                    .findViewById(R.id.item_play_finish_button);
        }

        public void bind() {
            mFinishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mOnItemActionListener.questCompleted();
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

        public void bind(){
            mCancelButton.setVisibility(View.INVISIBLE);
            mNextStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    nextStep();
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

        public void bind(@NonNull final JSONObject item) {
            String imagePath = null;
            try {
                imagePath = item.getString(IMAGE_);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!imagePath.isEmpty()) {
                Picasso.with(mContext)
                        .load(Uri.parse(imagePath)).resize(viewWidth, viewWidth)
                        .centerCrop().into(mImageView);
            }
        }
    }
}

