package com.tinycold.apm.ui;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

import androidx.annotation.NonNull;

/**
 * 卡顿监控
 */
public class CatonMonitor {
    /**
     * 使用Looper中的log日志监控方案
     */
    public static final int MONITOR_LOG = 1;
    /**
     * 使用空msg方案
     */
    public static final int MONITOR_MSG = 2;

    private static final CatonSummary mSummary = new CatonSummary();
    private static Checker mChecker = null;
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
                if (!(mChecker instanceof CheckerV1)) {
                    mChecker = new CheckerV1(mSummary);
                }
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
                break;
            }
            case MONITOR_MSG: {
                if (!(mChecker instanceof CheckerV2)) {
                    mChecker = new CheckerV2(mSummary);
                    mChecker.start();
                }
                break;
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
                mChecker.end();
                break;
            }
        }
        mChecker = null;
        mWay = 0;
    }

    public static @NonNull CatonSummary summary() {
        return mSummary;
    }

    private static abstract class Checker {
        protected final int CATON_INTERVAL = 1000;
        protected final CatonSummary mSummary;
        protected Checker(@NonNull CatonSummary summary) {
            mSummary = summary;
        }
        protected abstract void start();
        protected abstract void end();
    }

    private static class CheckerV1 extends Checker {
        private final HandlerThread mHandlerThread = new HandlerThread("CheckerV1");
        private final Handler mHandler;
        private final Runnable mRun = new Runnable() {
            @Override
            public void run() {
                // 执行卡顿时的统计
                mSummary.mCatonTimes++;
            }
        };

        private long mCheckStart = 0;

        public CheckerV1(@NonNull CatonSummary summary) {
            super(summary);
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
                mSummary.mCatonDuration += System.currentTimeMillis() - mCheckStart;
            }
            mCheckStart = 0;
        }

    }

    private static class CheckerV2 extends Checker {

        private final Handler mMainHandler;
        private CheckerV2Run mRun = null;
        private Thread mThread = null;

        protected CheckerV2(@NonNull CatonSummary summary) {
            super(summary);
            mMainHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        protected void start() {
            if (mThread != null || mRun != null) {
                end();
            }
            if (mThread == null) {
                mRun = new CheckerV2Run(mMainHandler, mSummary);
                mThread = new Thread(mRun);
                mThread.start();
            }
        }

        @Override
        protected void end() {
            if (mRun != null) {
                mRun.end();
                mRun = null;
            }
            if (mThread != null) {
                mThread.interrupt();
                mThread = null;
            }
        }
    }

    private static class CheckerV2Run implements Runnable {
        private boolean isInterrupt = false;
        private Handler mMainHandler;
        private CatonSummary mSummary;
        private static final int MSG_UI_EMPTY = 999;
        private int mLastMin = 0;

        private CheckerV2Run(@NonNull Handler handler, @NonNull CatonSummary summary) {
            mMainHandler = handler;
            mSummary = summary;
        }

        @Override
        public void run() {
            while (!isInterrupt) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!mMainHandler.hasMessages(MSG_UI_EMPTY)) {
                    if (mLastMin > 0) {
                        mSummary.mCatonTimes++;
                        mSummary.mCatonDuration += mLastMin * 1000;
                        mLastMin = 0;
                    }
                    mMainHandler.sendEmptyMessage(MSG_UI_EMPTY);
                    continue;
                }
                mLastMin++;
            }
        }

        public void end() {
            isInterrupt = true;
            mSummary = null;
        }

    }

}
