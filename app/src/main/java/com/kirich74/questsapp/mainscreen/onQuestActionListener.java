package com.kirich74.questsapp.mainscreen;

/**
 * Created by Kirill Pilipenko on 12.07.2017.
 */

public interface onQuestActionListener {
    void action(int id);

    String getButtonTitle ();

    void deleteQuest (int id, int globalId);
}
