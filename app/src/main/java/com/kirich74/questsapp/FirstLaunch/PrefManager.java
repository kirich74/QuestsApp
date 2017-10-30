package com.kirich74.questsapp.FirstLaunch;

import android.content.Context;
import android.content.SharedPreferences;

import static com.kirich74.questsapp.data.ItemType.NO_SAVED_EMAIL;

/**
 * Created by Kirill Pilipenko on 30.10.2017.
 */

public class PrefManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "saved_settings";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String SAVED_EMAIL = "SavedEmail";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setSavedEmail(String email){
        editor.putString(SAVED_EMAIL, email);
        editor.commit();
    }

    public String getSavedEmail(){
        return pref.getString(SAVED_EMAIL, NO_SAVED_EMAIL);
    }
}
