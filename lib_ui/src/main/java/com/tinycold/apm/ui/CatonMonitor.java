package com.tinycold.apm.ui;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

public class CatonMonitor {

    private static final Checker mChecker = new Checker();

    public static void start() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            private final String START = ">>>>> Dispatching to";
            private final String FINISH = "<<<<< Finished to";

            @Override
            public void println(String s) {
                if (s.startsWith(START)) {
                    mChecker.start();
                } else if (s.startsWith(FINISH)) {
                    mChecker.end();
                }
            }

        });
    }

    public static void end() {
        Looper.getMainLooper().setMessageLogging(null);
    }

    public static CatonDetail summary() {
        return mChecker.mDetail;
    }

    private static class Checker {
        private final int CATON_INTERVAL = 600;
        private final CatonDetail mDetail = new CatonDetail();
        private final HandlerThread mHandlerThread = new HandlerThread("Checker");
        private final Handler mHandler;
        private final Runnable mRun = new Runnable() {
            @Override
            public void run() {
                // 执行卡顿时的统计
                mDetail.mCatonTimes++;
            }
        };

        private long mCheckStart = 0;

        public Checker() {
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper());
        }

        public void start() {
            mCheckStart = System.currentTimeMillis();
            mHandler.postDelayed(mRun, CATON_INTERVAL);
        }

        public void end() {
            if (mHandler.hasCallbacks(mRun)) {
                mHandler.removeCallbacks(mRun);
            } else if (mCheckStart > 0) {
                mDetail.mCatonDuration += System.currentTimeMillis() - mCheckStart;
            }
            mCheckStart = 0;
        }

    }

    public static class CatonDetail {
        /**
         * 卡顿次数
         */
        public int mCatonTimes = 0;
        /**
         * 卡顿时长
         */
        public int mCatonDuration = 0;
    }

}
