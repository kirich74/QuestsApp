package com.kirich74.questsapp.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import java.util.ArrayList;

import static com.kirich74.questsapp.data.ItemType.IMAGE;
import static com.kirich74.questsapp.data.ItemType.IMAGE_;
import static com.kirich74.questsapp.data.ItemType.NEXT_STEP;
import static com.kirich74.questsapp.data.ItemType.NEXT_STEP_;
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

    public String mAuthor;

    public String mName;

    public String mDescription;

    public String mMainImageUri;

    public int mAccess;

    public int mGlobalId;

    public Quest() {
        quest = new ArrayList<>();
        mName = "";
        mDescription = "";
        mMainImageUri = "";
        mAccess = 0;
        mGlobalId = 0;
    }

    public void deleteItem (JSONObject item){
        quest.remove(item);
    }

    public Quest(final String name, final String description, final String mainImageUri,
            final int access, final String questInString, final int globalId) {
        try {
            mName = name;
            mDescription = description;
            mMainImageUri = mainImageUri;
            mAccess = access;
            mGlobalId = globalId;
            JSONArray questJsonArray = new JSONArray(questInString);
            quest = new ArrayList<>();
            for (int i = 0; i < questJsonArray.length(); i++) {
                quest.add(questJsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getGlobalId() {
        return mGlobalId;
    }

    public void setGlobalId(final int globalId) {
        mGlobalId = globalId;
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

    public void addNextItem() {
        JSONObject object = new JSONObject();
        try {
            object.put(TYPE, NEXT_STEP);
            object.put(NEXT_STEP_, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        quest.add(object);
    }

    public Uri getImageUri(int position) {
        try {
            return Uri.parse(quest.get(realPosition(position)).getString(IMAGE_));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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

    public String getMainImageUri() {
        return mMainImageUri;
    }

    private int realPosition(int position) {
        return position - 1;
    }
}
