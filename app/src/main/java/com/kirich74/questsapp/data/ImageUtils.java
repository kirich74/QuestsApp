package com.kirich74.questsapp.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Kirill Pilipenko on 26.08.2017.
 */

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    private static int RESULT_LOAD_IMAGE = 1;


    public static Uri saveBitmapToFile(@NonNull final Context context,
            @NonNull final Bitmap image, @NonNull final int globalId, @NonNull final int step) {
        BufferedOutputStream stream = null;
        File file;
        try {
            file = FileUtils.createJpgFile(context, globalId, step);
            stream = new BufferedOutputStream(new FileOutputStream(file));
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return Uri.parse(file.toURI().toString());
        } catch (IOException e) {
            Log.e(TAG, "saveBitmapToFile: file not created", e);
            return null;
        } finally {
            StreamUtils.close(stream);
        }
    }

}
