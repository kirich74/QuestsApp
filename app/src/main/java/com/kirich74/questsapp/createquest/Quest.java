package com.kirich74.questsapp.createquest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class Quest {

    private JSONArray quest;

    public void createNewQuest(String name){
        quest = new JSONArray();
        quest.put(name);
    }

    public void addItem (JSONObject item){
        quest.put(item);
    }

    public JSONObject getItem(int i){
        try {
            return quest.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
