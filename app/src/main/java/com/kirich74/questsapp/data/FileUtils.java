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
    public static File createJpgFile(@NonNull final Context context, @NonNull final int globalId,
            @NonNull final int step) throws IOException {

        String imageFileName = "JPEG_" + globalId + "_" + step + "_";
        File storageDir = context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + globalId);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static void deleteFile(@NonNull final Context context, @NonNull String path) {
        path = path.substring(5);
        File fdelete = new File(path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + path);
            } else {
                System.out.println("file not Deleted :" + path);
            }
        }
    }

}
