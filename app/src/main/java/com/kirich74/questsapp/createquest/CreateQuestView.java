package com.kirich74.questsapp.createquest;

import com.arellomobile.mvp.MvpView;
import com.kirich74.questsapp.data.Quest;

import android.content.ContentValues;
import android.net.Uri;

public interface CreateQuestView extends MvpView {


    void onSaveQuest(ContentValues values, Uri currentQuestUri);

    void showQuestRecyclerView(Quest quest);
}
