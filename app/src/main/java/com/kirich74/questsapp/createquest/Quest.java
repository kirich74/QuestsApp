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

    private String name;
    private ArrayList<JSONObject> quest;

    public Quest(){
        quest = new ArrayList<>();
    }

    public Quest(final String questInString) {
        try {
            JSONArray questJsonArray = new JSONArray(questInString);
            quest = new ArrayList<>();
            for (int i = 0; i < questJsonArray.length(); i++){
                quest.add(questJsonArray.getJSONObject(i));
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
        quest.add(object);
    }

    public void editTextItem (int position, String text){
        quest.get(position).remove(TEXT_);
        try {
            quest.get(position).put(TEXT_, text);
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
        quest.add(object);
    }

    public void editTextAnswerItem (int position, String text){
        quest.get(position).remove(TEXT_ANSWER_);
        try {
            quest.get(position).put(TEXT_ANSWER_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getItem(int i){
        return quest.get(i);
    }

    public int size () {
        return quest.size();
    }

    public String getQuestJsonArrayString (){
        return new JSONArray(quest).toString();
    }
}
