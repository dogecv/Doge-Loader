package com.coltonrobotics.dogeloader;

import android.os.Environment;

import java.io.File;

public class FileUtil {
    public static void Delete(String jarname){
        File toCheckLib = new File(Environment.getExternalStorageDirectory().toString() + "/FIRST/java/lib/",jarname);
        File toCheckSrc = new File(Environment.getExternalStorageDirectory().toString() + "/FIRST/java/src/jars/",jarname);

        toCheckLib.delete();
        toCheckSrc.delete();
    }

    public static boolean isInstalled(String jarname){
        File toCheckLib = new File(Environment.getExternalStorageDirectory().toString() + "/FIRST/java/lib/",jarname);
        File toCheckSrc = new File(Environment.getExternalStorageDirectory().toString() + "/FIRST/java/src/jars/",jarname);

        return toCheckLib.exists() && toCheckSrc.exists();
    }
}
