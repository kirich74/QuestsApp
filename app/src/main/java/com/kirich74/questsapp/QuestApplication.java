package com.kirich74.questsapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by Kirill Pilipenko on 26.08.2017.
 */

public class QuestApplication extends Application {

    static final String TAG = "ReceiptCaptureApplication";

    private static QuestApplication INSTANCE;

    private static Context context;

    public static Context getAppContext() {
        return QuestApplication.context;
    }

    public static QuestApplication getApplication() {
        return INSTANCE;
    }
}
