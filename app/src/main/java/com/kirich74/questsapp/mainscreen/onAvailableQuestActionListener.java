package com.kirich74.questsapp.mainscreen;

/**
 * Created by Kirill Pilipenko on 12.07.2017.
 */

public interface onAvailableQuestActionListener {
    void download(int id);

    String getButtonTitle(int id);

    void deleteQuest(int id);

    void deleteAccess(int id);
}
