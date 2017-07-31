package io.javac.ManyBlue.utils;

import android.util.Log;

/**
 * Created by Pencilso on 2017/7/23.
 */

public class LogUtils {
    public static void log(Object object){
        Log.e("ManyBlue",object==null?"null":object.toString());
    }
}
