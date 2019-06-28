package com.tinycold.apm.ui;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

public class CatonMonitor {
    /**
     * 使用Looper中的log日志监控方案
     */
    public static final int MONITOR_LOG = 1;
    /**
     * 使用空msg方案
     */
    public static final int MONITOR_MSG = 2;

    private static final CheckerV1 mCheckerV1 = new CheckerV1();
    private static int mWay = 0;

    public static void start(int way) {
        if (mWay == way) {
            return;
        }
        if (mWay != 0) {
            end();
        }
        mWay = way;
        switch (mWay) {
            case MONITOR_LOG: {
                Looper.getMainLooper().setMessageLogging(new Printer() {
                    private final String START = ">>>>> Dispatching to";
                    private final String FINISH = "<<<<< Finished to";

                    @Override
                    public void println(String s) {
                        if (s.startsWith(START)) {
                            mCheckerV1.start();
                        } else if (s.startsWith(FINISH)) {
                            mCheckerV1.end();
                        }
                    }

                });
                break;
            }
            case MONITOR_MSG: {

            }
        }
    }

    public static void end() {
        switch (mWay) {
            case MONITOR_LOG: {
                Looper.getMainLooper().setMessageLogging(null);
                break;
            }
            case MONITOR_MSG: {
                break;
            }
        }
    }

    public static CatonSummary summary() {
        return mCheckerV1.mDetail;
    }

    private static class CheckerV1 {
        private final int CATON_INTERVAL = 600;
        private final CatonSummary mDetail = new CatonSummary();
        private final HandlerThread mHandlerThread = new HandlerThread("CheckerV1");
        private final Handler mHandler;
        private final Runnable mRun = new Runnable() {
            @Override
            public void run() {
                // 执行卡顿时的统计
                mDetail.mCatonTimes++;
            }
        };

        private long mCheckStart = 0;

        public CheckerV1() {
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

    private static class CheckerV2 {
        
    }

}
