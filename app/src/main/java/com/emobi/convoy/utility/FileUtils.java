package com.emobi.convoy.utility;

import android.os.Environment;

import java.io.File;

/**
 * Created by sunil on 19-01-2017.
 */

public class FileUtils {
    public static String BASE_FILE_PATH= Environment.getExternalStorageDirectory().toString()+ File.separator+"convoy";

}
