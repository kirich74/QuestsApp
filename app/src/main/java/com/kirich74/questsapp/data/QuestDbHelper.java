package com.kirich74.questsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.kirich74.questsapp.data.QuestContract.QuestEntry;

/**
 * Created by Kirill Pilipenko on 12.07.2017.
 */

public class QuestDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "localQuests.db";

    private static final int DATABASE_VERSION = 1;

    public QuestDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        String SQL_CREATE_QUESTS_TABLE =  "CREATE TABLE " + QuestEntry.TABLE_NAME + " ("
                + QuestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuestEntry.COLUMN_QUEST_GLOBAL_ID + " INTEGER, "
                + QuestEntry.COLUMN_QUEST_NAME + " TEXT NOT NULL, "
                + QuestEntry.COLUMN_QUEST_AUTHOR + " TEXT NOT NULL, "
                + QuestEntry.COLUMN_QUEST_IMAGE + " TEXT, "
                + QuestEntry.COLUMN_QUEST_DATA_JSON + " TEXT, "
                + QuestEntry.COLUMN_QUEST_ACCESS + " INTEGER NOT NULL, "
                + QuestEntry.COLUMN_QUEST_DESCRIPTION + " TEXT);";

        db.execSQL(SQL_CREATE_QUESTS_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestEntry.TABLE_NAME);
        onCreate(db);
    }
}
