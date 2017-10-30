package com.kirich74.questsapp.playquest;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kirich74.questsapp.data.Quest;
import com.kirich74.questsapp.data.QuestContract;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;

@InjectViewState
public class PlayQuestPresenter extends MvpPresenter<PlayQuestView> {
    private Quest mQuest;
    private Uri mCurrentQuestUri;
    private int begin, end;

    public void setQuest(Uri questUri, String name, String description, String image, String dataJson, int access) {
        if (dataJson == null) {
            mQuest = new Quest();
        } else {
            mQuest = new Quest(name, description, image, access, dataJson, 0);
        }
        mCurrentQuestUri = questUri;
        getViewState().showStepRecyclerView(mQuest, begin, end);
    }

}
