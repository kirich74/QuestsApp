package com.kirich74.questsapp.data;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * Created by Kirill Pilipenko on 26.08.2017.
 */

public class StreamUtils {

    private static final String TAG = "StreamUtils";

    private StreamUtils() {

    }

    public static void close(@Nullable Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            if (closeable instanceof Flushable) {
                ((Flushable) closeable).flush();
            }

            closeable.close();
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        }
    }

}
