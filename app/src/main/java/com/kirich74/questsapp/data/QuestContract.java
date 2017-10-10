package com.kirich74.questsapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kirill Pilipenko on 12.07.2017.
 */

public final class QuestContract {

    private QuestContract(){}

    public static final String CONTENT_AUTHORITY = "ru.kirich74.quests";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_QUESTS = "quests";

    public static final class QuestEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_QUESTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTS;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTS;

        public final static String TABLE_NAME = "localQuests";

        /**
         * Unique ID number for the quest (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the quest.
         *
         * Type: TEXT
         */
        public final static String COLUMN_QUEST_NAME ="name";

        /**
         * Author of the quest.
         *
         * Type: TEXT
         */
        public final static String COLUMN_QUEST_AUTHOR = "author";

        /**
         * Description of the quest.
         *
         * Type: TEXT
         */
        public final static String COLUMN_QUEST_DESCRIPTION = "description";

        /**
         * Image of the quest.
         *
         * Type: TEXT
         */
        public final static String COLUMN_QUEST_IMAGE = "image";

        /**
         * Data json string of the quest.
         *
         * Type: TEXT
         */
        public final static String COLUMN_QUEST_DATA_JSON = "json";

        /**
         * Access.
         *
         * The only possible values are {@link #PLAY} or {@link #CREATE}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_QUEST_ACCESS = "access";


        /**
         * Possible values for the access of the quest.
         */
        public static final int PLAY = 0;
        public static final int CREATE = 1;

        /**
         * Returns whether or not the given access is {@link #PLAY}, {@link #CREATE}.
         */
        public static boolean isValidAccess(int access) {
            if (access == PLAY || access == CREATE) {
                return true;
            }
            return false;
        }
    }
}
