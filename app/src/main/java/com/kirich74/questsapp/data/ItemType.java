package com.kirich74.questsapp.data;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class ItemType {
    public static final int UNKNOWN_TYPE = -1;
    public static final int TEXT = 0;
    public static final int IMAGE = 2;
    public static final int NAME = 3;
    public static final int DESCRIPTION = 4;
    public static final int ADD_BUTTONS = 10;
    public static final int MAIN_INFO = 11;
    public static final int QUEST_FINISHED = 12;

    // All answers and only them must be bigger than ANSWER
    public static final int ANSWER = 30;
    public static final int TEXT_ANSWER = 31;
    public static final int NEXT_STEP = 32;
    public static final int QR_CODE = 33;

    public static final String TYPE = "type";
    public static final String TEXT_ = "text";
    public static final String TEXT_ANSWER_ = "text answer";
    public static final String IMAGE_ = "image";
    public static final String NEXT_STEP_ = "next step";
    public static final String QR_CODE_ = "qr code";
    public static final String NO_SAVED_EMAIL = "NO_SAVED_EMAIL";



    public static final int AVAILABLE_FOR_ALL = 0;
    public static final int AVAILABLE_FOR_ME = 1;
    public static final int MY_QUESTS = 2;

    private ItemType() {
    }
}
