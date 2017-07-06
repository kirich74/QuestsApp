package com.kirich74.questsapp.createquest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class Item {
    JSONObject item;

    public JSONObject createTextItem(String text){
        try {
            item.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject createTextAnswerItem(String text){
        try {
            item.put("text answer", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
