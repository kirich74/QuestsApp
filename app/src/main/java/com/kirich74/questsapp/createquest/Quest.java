package com.kirich74.questsapp.createquest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.kirich74.questsapp.data.ItemType.TEXT;
import static com.kirich74.questsapp.data.ItemType.TEXT_;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER;
import static com.kirich74.questsapp.data.ItemType.TEXT_ANSWER_;
import static com.kirich74.questsapp.data.ItemType.TYPE;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class Quest {

    private JSONArray quest;
    private String name;
    private ArrayList<JSONObject> questList;

    public Quest(){
        quest = new JSONArray();
        questList = new ArrayList<>();
    }

    public Quest(final String questInString) {
        try {
            quest = new JSONArray(questInString);
            questList = new ArrayList<>();
            for (int i = 0; i < quest.length(); i++){
                questList.add(quest.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addTextItem (String text){
        JSONObject object = new JSONObject();
        try {
            object.put(TYPE, TEXT);
            object.put(TEXT_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questList.add(object);
    }

    public void editTextItem (int position, String text){
        questList.get(position).remove(TEXT_);
        try {
            questList.get(position).put(TEXT_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addTextAnswerItem (String text){
        JSONObject object = new JSONObject();
        try {
            object.put(TYPE, TEXT_ANSWER);
            object.put(TEXT_ANSWER_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        questList.add(object);
    }

    public void editTextAnswerItem (int position, String text){
        questList.get(position).remove(TEXT_ANSWER_);
        try {
            questList.get(position).put(TEXT_ANSWER_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getItem(int i){
        return questList.get(i);
    }

    public int size () {
        return questList.size();
    }
}
