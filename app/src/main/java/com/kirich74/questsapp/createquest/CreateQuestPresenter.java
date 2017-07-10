package com.kirich74.questsapp.createquest;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;


@InjectViewState
public class CreateQuestPresenter extends MvpPresenter<CreateQuestView> {

    private Quest mQuest;

    public void setQuest(String questInString) {
        if (questInString == null) {
            mQuest = new Quest();
        } else {
            mQuest = new Quest(questInString);
        }
        getViewState().showQuestRecyclerView(mQuest);
    }

    public void saveQuest(){

    }




}
