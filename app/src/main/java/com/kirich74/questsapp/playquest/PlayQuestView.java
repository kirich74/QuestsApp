package com.kirich74.questsapp.playquest;

import com.arellomobile.mvp.MvpView;
import com.kirich74.questsapp.data.Quest;

public interface PlayQuestView extends MvpView {

    void showStepRecyclerView(Quest quest, int begin, int end);

}
