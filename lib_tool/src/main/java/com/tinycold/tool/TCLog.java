package com.tinycold.tool;

import android.util.Log;

@SuppressWarnings("unused")
public class TCLog {
    private static final String TAG_DEBUG = "TC_DEBUG";
    private static final String TAG_ERROR = "TC_ERROR";

    public static void debug(String log) {
        Log.d(TAG_DEBUG, String.format("[%d] %s", System.currentTimeMillis(), log));
    }

    public static void error(String log) {
        Log.e(TAG_ERROR, String.format("[%d] %s", System.currentTimeMillis(), log));
    }

}
