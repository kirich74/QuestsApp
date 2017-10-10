package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.QuestContract;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill Pilipenko on 12.07.2017.
 */

public class QuestsRecyclerViewAdapter
        extends RecyclerView.Adapter<QuestsRecyclerViewAdapter.ViewHolder> {

    private final int viewWidth;

    private onQuestActionListener mOnQuestActionListener;

    private Cursor mCursor;

    private Context mContext;

    public QuestsRecyclerViewAdapter(onQuestActionListener onQuestActionListener, Context context,
            Cursor cursor) {
        mOnQuestActionListener = onQuestActionListener;
        mCursor = cursor;
        mContext = context;
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        viewWidth = windowManager.getDefaultDisplay().getWidth();
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_quest, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bindCursor(mCursor);
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0
                : mCursor.getCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        int id;

        private TextView name, author, description;

        private ImageView imageDescription;

        private Button startOrCreateButton;

        private ImageButton deleteButton;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.quest_title_text_view);
            author = (TextView) itemView.findViewById(R.id.quest_author_text_view);
            description = (TextView) itemView.findViewById(R.id.quest_description_text_view);
            imageDescription = (ImageView) itemView.findViewById(R.id.image_description);
            startOrCreateButton = (Button) itemView.findViewById(R.id.quest_start_or_edit_button);
            deleteButton = (ImageButton) itemView.findViewById(R.id.quest_delete_button);
        }

        public void bindCursor(final Cursor cursor) {
            id = mCursor.getInt(mCursor.getColumnIndexOrThrow(QuestContract.QuestEntry._ID));
            name.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(QuestContract.QuestEntry.COLUMN_QUEST_NAME)));
            String imagePath = cursor.getString(
                    cursor.getColumnIndexOrThrow(QuestContract.QuestEntry.COLUMN_QUEST_IMAGE));
            if (!imagePath.isEmpty()) {
                Picasso.with(mContext).load(imagePath).resize(viewWidth, viewWidth).centerCrop()
                        .into(imageDescription);
            }
            description.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            QuestContract.QuestEntry.COLUMN_QUEST_DESCRIPTION)));
            author.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(QuestContract.QuestEntry.COLUMN_QUEST_AUTHOR)));
            startOrCreateButton.setText(mOnQuestActionListener.getButtonTitle());
            startOrCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mOnQuestActionListener.startOrEdit(id);
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    mOnQuestActionListener.deleteQuest(id);
                }
            });
        }
    }

}
