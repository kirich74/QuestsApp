package com.kirich74.questsapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.kirich74.questsapp.data.QuestContract.QuestEntry;

/**
 * Created by Kirill Pilipenko on 12.07.2017.
 */

public class QuestProvider extends ContentProvider {

    public static final String LOG = "QuestProvider";

    /** URI matcher code for the content URI for the quests table */
    private static final int QUESTS = 100;

    /** URI matcher code for the content URI for a single quest in the quests table */
    private static final int QUEST_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.quests/quests" will map to the
        // integer code {@link #QUESTS}. This URI is used to provide access to MULTIPLE rows
        // of the quests table.
        sUriMatcher.addURI(QuestContract.CONTENT_AUTHORITY, QuestContract.PATH_QUESTS, QUESTS);

        // The content URI of the form "content://com.example.android.quests/quests/#" will map to the
        // integer code {@link #QUEST_ID}. This URI is used to provide access to ONE single row
        // of the quests table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.quests/quests/3" matches, but
        // "content://com.example.android.quests/quests" (without a number at the end) doesn't match.
        sUriMatcher.addURI(QuestContract.CONTENT_AUTHORITY, QuestContract.PATH_QUESTS + "/#", QUEST_ID);
    }

    /** Database helper object */
    private QuestDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new QuestDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case QUESTS:
                // For the QUESTS code, query the quests table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the quests table.
                cursor = database.query(QuestEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case QUEST_ID:
                // For the QUEST_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.quests/quests/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = QuestEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the quests table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(QuestEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case QUESTS:
                return insertQuest(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertQuest(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(QuestEntry.COLUMN_QUEST_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Quest requires a name");
        }

        // Check that the author is not null
        String author = values.getAsString(QuestEntry.COLUMN_QUEST_AUTHOR);
        if (author == null) {
            throw new IllegalArgumentException("Quest requires a author");
        }

        // Check that the access is valid
        Integer access = values.getAsInteger(QuestEntry.COLUMN_QUEST_ACCESS);
        if (access == null || !QuestEntry.isValidAccess(access)) {
            throw new IllegalArgumentException("Quest requires valid access");
        }


        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new quest with the given values
        long id = database.insert(QuestEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the quest content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
            String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case QUESTS:
                return updateQuest(uri, contentValues, selection, selectionArgs);
            case QUEST_ID:
                // For the QUEST_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = QuestEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateQuest(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update quests in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more quests).
     * Return the number of rows that were successfully updated.
     */
    private int updateQuest(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // check that the name value is not null.
        if (values.containsKey(QuestEntry.COLUMN_QUEST_NAME)) {
            String name = values.getAsString(QuestEntry.COLUMN_QUEST_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Quest requires a name");
            }
        }

        // check that the author value is not null.
        if (values.containsKey(QuestEntry.COLUMN_QUEST_AUTHOR)) {
            String author = values.getAsString(QuestEntry.COLUMN_QUEST_AUTHOR);
            if (author == null) {
                throw new IllegalArgumentException("Quest requires a author");
            }
        }

        // check that the access value is valid.
        if (values.containsKey(QuestEntry.COLUMN_QUEST_ACCESS)) {
            Integer access = values.getAsInteger(QuestEntry.COLUMN_QUEST_ACCESS);
            if (access == null || !QuestEntry.isValidAccess(access)) {
                throw new IllegalArgumentException("Quest requires valid access");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(QuestEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case QUESTS:
                // DeleteUpdate all rows that match the selection and selection args
                rowsDeleted = database.delete(QuestEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case QUEST_ID:
                // DeleteUpdate a single row given by the ID in the URI
                selection = QuestEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(QuestEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case QUESTS:
                return QuestEntry.CONTENT_LIST_TYPE;
            case QUEST_ID:
                return QuestEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
