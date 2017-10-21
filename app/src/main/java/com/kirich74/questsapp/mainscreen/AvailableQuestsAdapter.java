package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.cloudclient.models.AvailableQuest;
import com.kirich74.questsapp.data.Quest;
import com.kirich74.questsapp.data.QuestContract;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kirill Pilipenko on 21.10.2017.
 */

public class AvailableQuestsAdapter
        extends RecyclerView.Adapter<AvailableQuestsAdapter.AvailableQuestsViewHolder> {

    private List<AvailableQuest> mAvailableQuests;

    private onQuestActionListener mOnQuestActionListener;

    private Context mContext;

    private boolean isAvailableForMe;

    private final int viewWidth;

    public AvailableQuestsAdapter (onQuestActionListener onQuestActionListener, Context context){
        mOnQuestActionListener = onQuestActionListener;
        mContext = context;
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        viewWidth = windowManager.getDefaultDisplay().getWidth();
    }

    public List<AvailableQuest> getQuests() {
        return mAvailableQuests;
    }

    public void setAvailableQuests(@NonNull final List<AvailableQuest> quests, boolean isAvailableForMe) {
        mAvailableQuests = quests;
        this.isAvailableForMe = isAvailableForMe;
        notifyDataSetChanged();
    }

    @Override
    public AvailableQuestsViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_quest, viewGroup, false);
        return new AvailableQuestsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AvailableQuestsViewHolder holder,final int position) {
        if (position >= 0 && position < getItemCount()) {
            holder.bind(mAvailableQuests.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mAvailableQuests == null ? 0
                : mAvailableQuests.size();
    }

    public class AvailableQuestsViewHolder extends RecyclerView.ViewHolder {

        int id;

        private TextView name, author, description;

        private ImageView imageDescription;

        private Button actionButton;

        private ImageButton deleteButton;

        public AvailableQuestsViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.quest_title_text_view);
            author = (TextView) itemView.findViewById(R.id.quest_author_text_view);
            description = (TextView) itemView.findViewById(R.id.quest_description_text_view);
            imageDescription = (ImageView) itemView.findViewById(R.id.image_description);
            actionButton = (Button) itemView.findViewById(R.id.quest_action_button);
            deleteButton = (ImageButton) itemView.findViewById(R.id.quest_delete_button);
        }


        public void bind(@NonNull final AvailableQuest quest){
            id = quest.getId();
            name.setText(quest.getName());
            String imagePath = quest.getImageUri();
            if (!imagePath.isEmpty()) {
                Picasso.with(mContext).load(imagePath).resize(viewWidth, viewWidth).centerCrop()
                        .into(imageDescription);
            }
            description.setText(quest.getDescription());
            author.setText(quest.getEmail());
            actionButton.setText(mOnQuestActionListener.getButtonTitle());
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mOnQuestActionListener.action(id);
                }
            });
            if (isAvailableForMe){
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mOnQuestActionListener.deleteQuest(id);
                    }
                });
            }
            else {
                deleteButton.setVisibility(View.INVISIBLE);
            }
        }
    }
}
