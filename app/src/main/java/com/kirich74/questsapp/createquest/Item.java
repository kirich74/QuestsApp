package com.kirich74.questsapp.createquest;

import org.json.JSONException;
import org.json.JSONObject;
import static com.kirich74.questsapp.data.ItemType.*;

/**
 * Created by Kirill Pilipenko on 06.07.2017.
 */

public class Item {

    public Item() {
        item = new JSONObject();
    }

    private JSONObject item;

    public JSONObject getItem() {
        return item;
    }

    public JSONObject createTextItem(String text){
        try {
            item.put(TYPE, TEXT);
            item.put(TEXT_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject createTextAnswerItem(String text){
        try {
            item.put(TYPE, TEXT_ANSWER);
            item.put(TEXT_ANSWER_, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
