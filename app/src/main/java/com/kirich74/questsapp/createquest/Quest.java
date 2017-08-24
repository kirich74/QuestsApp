package com.kirich74.questsapp.createquest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.kirich74.questsapp.data.ItemType.IMAGE;
import static com.kirich74.questsapp.data.ItemType.IMAGE_;
import static com.kirich74.questsapp.data.ItemType.TEXT;
import static com.kirich74.questsapp.data.ItemType.TEXT_;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER_;
import static com.kirich74.questsapp.data.ItemType.TYPE;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class Quest {

    public ArrayList<JSONObject> quest;

    public String mName;

    public String mDescription;

    public String mMainImageUri;

    public int mAccess;

    public Quest() {
        quest = new ArrayList<>();
        mName = "";
        mDescription = "";
        mMainImageUri = "";
        mAccess = 0;
    }

    public Quest(final String name, final String description, final String mainImageUri,
            final int access, final String questInString) {
        try {
            mName = name;
            mDescription = description;
            mMainImageUri = mainImageUri;
            mAccess = access;
            JSONArray questJsonArray = new JSONArray(questInString);
            quest = new ArrayList<>();
            for (int i = 0; i < questJsonArray.length(); i++) {
                quest.add(questJsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addTextItem() {
        JSONObject object = new JSONObject();
        try {
            object.put(TYPE, TEXT);
            object.put(TEXT_, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        quest.add(object);
    }

    public void editTextItem(int position, String text) {
        quest.get(realPosition(position)).remove(TEXT_);
        try {
            quest.get(realPosition(position)).put(TEXT_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addTextAnswerItem() {
        JSONObject object = new JSONObject();
        try {
            object.put(TYPE, TEXT_ANSWER);
            object.put(TEXT_ANSWER_, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        quest.add(object);
    }

    public void editTextAnswerItem(int position, String text) {
        quest.get(realPosition(position)).remove(TEXT_ANSWER_);
        try {
            quest.get(realPosition(position)).put(TEXT_ANSWER_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addImageItem() {
        JSONObject object = new JSONObject();
        try {
            object.put(TYPE, IMAGE);
            object.put(IMAGE_, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        quest.add(object);
    }

    public void editImageItem(int position, String image) {
        quest.get(realPosition(position)).remove(IMAGE_);
        try {
            quest.get(realPosition(position)).put(IMAGE_, image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject getItem(int i) {
        return quest.get(realPosition(i));
    }

    public int size() {
        return quest.size();
    }

    public String getQuestJsonArrayString() {
        return new JSONArray(quest).toString();
    }

    private int realPosition(int position) {
        return position - 1;
    }
}
