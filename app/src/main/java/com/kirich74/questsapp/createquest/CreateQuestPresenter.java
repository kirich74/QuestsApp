package com.kirich74.questsapp.createquest;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.data.QuestContract.QuestEntry;


@InjectViewState
public class CreateQuestPresenter extends MvpPresenter<CreateQuestView> {

    private Quest mQuest;
    private String name;
    private String description;
    private Uri image;
    private int access;
    private Uri mCurrentQuestUri;

    public void setQuest(Uri questUri, String name, String description, String image, String dataJson, int access) {
        if (dataJson == null) {
            mQuest = new Quest();
        } else {
            mQuest = new Quest(dataJson);
        }
        mCurrentQuestUri = questUri;
        this.name = name;
        this.description = description;
        this.image = Uri.parse(image);
        this.access = access;
        getViewState().showQuestRecyclerView(mQuest);
    }

    public void CreateEmptyQuest (){
        mQuest = new Quest();
        getViewState().showQuestRecyclerView(mQuest);
    }



    public void saveQuest() {
        String questData = mQuest.getQuestJsonArrayString();
        // Check if this is supposed to be a new quest
        // and check if all the fields in the editor are blank
        if (mCurrentQuestUri == null &&
                TextUtils.isEmpty(name) && TextUtils.isEmpty(description) &&
                TextUtils.isEmpty(questData)) {
            // Since no fields were modified, we can return early without creating a new quest.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and quest attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(QuestEntry.COLUMN_QUEST_NAME, "test");  //TODO make normal view
        values.put(QuestEntry.COLUMN_QUEST_DESCRIPTION, description);
        values.put(QuestEntry.COLUMN_QUEST_AUTHOR, "kirich74");
        values.put(QuestEntry.COLUMN_QUEST_ACCESS, 0);
        values.put(QuestEntry.COLUMN_QUEST_DATA_JSON, questData);
        values.put(QuestEntry.COLUMN_QUEST_IMAGE, "test");

        getViewState().onSaveQuest(values, mCurrentQuestUri);
    }


}
