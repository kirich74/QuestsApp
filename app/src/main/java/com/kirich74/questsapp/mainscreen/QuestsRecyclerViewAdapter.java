package com.kirich74.questsapp.mainscreen;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.QuestContract;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kirill Pilipenko on 12.07.2017.
 */

public class QuestsRecyclerViewAdapter
        extends  CursorAdapter {

    private onQuestActionListener mOnQuestActionListener;

    public QuestsRecyclerViewAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    public void setOnQuestActionListener (onQuestActionListener onQuestActionListener){
        mOnQuestActionListener = onQuestActionListener;
    }


    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_quest, parent, false);
    }

    @Override
    public void bindView(final View itemView, final Context context, final Cursor cursor) {
        TextView name, author, description;

        ImageView imageDescription;

        Button startOrCreateButton;

        final int id;

        name = (TextView) itemView.findViewById(R.id.quest_title_text_view);
        author = (TextView) itemView.findViewById(R.id.quest_author_text_view);
        description = (TextView) itemView.findViewById(R.id.quest_description_text_view);
        imageDescription = (ImageView) itemView.findViewById(R.id.image_description);
        startOrCreateButton = (Button) itemView.findViewById(R.id.quest_start_or_edit_button);

        id = cursor.getInt(cursor.getColumnIndexOrThrow(QuestContract.QuestEntry._ID));
        name.setText(cursor.getString(
                cursor.getColumnIndexOrThrow(QuestContract.QuestEntry.COLUMN_QUEST_NAME)));
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
    }

}
