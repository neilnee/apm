package com.tinycold.apm.tool;

import android.util.Log;

public class APMLog {
    private static final String TAG_DEBUG = "APM_DEBUG";

    public static void debug(String log) {
        Log.d(TAG_DEBUG, String.format("[%d] %s", System.currentTimeMillis(), log));
    }

}
