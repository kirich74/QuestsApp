package com.kirich74.questsapp.data;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kirill Pilipenko on 26.08.2017.
 */

public class FileUtils {

    private FileUtils() {

    }

    @NonNull
    public static File createJpgFile(@NonNull final Context context, @NonNull final int globalId, @NonNull final int step) throws IOException {

        String imageFileName = "JPEG_" + globalId + "_" + step + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

}
