package com.kirich74.questsapp.data;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class ItemType {
    public static final int UNKNOWN_TYPE = -1;
    public static final int TEXT = 0;
    public static final int TEXT_ANSWER = 1;
    public static final int IMAGE_URI = 2;

    public static final String TYPE = "type";
    public static final String TEXT_ = "text";
    public static final String TEXT_ANSWER_ = "text answer";
    private ItemType() {
    }
}