package com.kirich74.questsapp.createquest;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kirich74.questsapp.data.Quest;
import com.kirich74.questsapp.data.QuestContract.QuestEntry;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;


@InjectViewState
public class CreateQuestPresenter extends MvpPresenter<CreateQuestView> {

    private Quest mQuest;

    private Uri mCurrentQuestUri;

    public void setQuest(Uri questUri, String name, String description, String image,
            String dataJson, int access, int globalId) {
        mQuest = new Quest(name, description, image, access, dataJson, globalId);
        mCurrentQuestUri = questUri;
        getViewState().showQuestRecyclerView(mQuest);
    }

    public void CreateEmptyQuest() {
        mQuest = new Quest();
        getViewState().showQuestRecyclerView(mQuest);
    }


    public void saveQuest() {
        // Check if this is supposed to be a new quest
        // and check if all the fields in the editor are blank
        if (mCurrentQuestUri == null &&
                TextUtils.isEmpty(mQuest.mName) && TextUtils.isEmpty(mQuest.mDescription)) {
            // Since no fields were modified, we can return early without creating a new quest.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and quest attributes from the editor are the values.


        getViewState().onSaveQuest(mQuest, mCurrentQuestUri);
    }


}
