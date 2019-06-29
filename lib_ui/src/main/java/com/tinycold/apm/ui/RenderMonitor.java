package com.tinycold.apm.ui;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

import androidx.annotation.NonNull;

/**
 * 卡顿监控
 */
public class RenderMonitor {
    /**
     * 使用Looper中log日志方案进行卡顿监控
     */
    public static final int MONITOR_CATON_BY_LOG = 1;
    /**
     * 使用空msg方案进行卡顿监控
     */
    public static final int MONITOR_CATON_BY_MSG = 2;

    // 卡顿监控相关
    private static final CatonSummary mCatonSummary = new CatonSummary();
    private static CatonChecker mCatonChecker = null;
    private static int mCatonWay = 0;

    // 帧率监控相关
    private static final FrameSummary mFrameSummary = new FrameSummary();

    /**
     * 启动卡顿监控
     * @param way 监控方案
     */
    public static void monitorCaton(int way) {
        if (mCatonWay == way) {
            return;
        }
        if (mCatonWay != 0) {
            endCaton();
        }
        mCatonWay = way;
        switch (mCatonWay) {
            case MONITOR_CATON_BY_LOG: {
                if (!(mCatonChecker instanceof CatonCheckerV1)) {
                    mCatonChecker = new CatonCheckerV1(mCatonSummary);
                }
                Looper.getMainLooper().setMessageLogging(new Printer() {
                    private final String START = ">>>>> Dispatching to";
                    private final String FINISH = "<<<<< Finished to";

                    @Override
                    public void println(String s) {
                        if (s.startsWith(START)) {
                            mCatonChecker.start();
                        } else if (s.startsWith(FINISH)) {
                            mCatonChecker.end();
                        }
                    }

                });
                break;
            }
            case MONITOR_CATON_BY_MSG: {
                if (!(mCatonChecker instanceof CatonCheckerV2)) {
                    mCatonChecker = new CatonCheckerV2(mCatonSummary);
                    mCatonChecker.start();
                }
                break;
            }
        }
    }

    /**
     * 停止卡顿监控
     */
    public static void endCaton() {
        switch (mCatonWay) {
            case MONITOR_CATON_BY_LOG: {
                Looper.getMainLooper().setMessageLogging(null);
                break;
            }
            case MONITOR_CATON_BY_MSG: {
                mCatonChecker.end();
                break;
            }
        }
        mCatonChecker = null;
        mCatonWay = 0;
    }

    /**
     * 获取卡顿监控信息
     */
    public static @NonNull CatonSummary catonSummary() {
        return mCatonSummary;
    }

    /**
     * 启动帧率监控
     */
    public static void monitorFrame() {

    }

    /**
     * 停止帧率监控
     */
    public static void endFrame() {

    }

    private static abstract class CatonChecker {
        protected final int CATON_INTERVAL = 1000;
        protected final CatonSummary mSummary;
        protected CatonChecker(@NonNull CatonSummary summary) {
            mSummary = summary;
        }
        protected abstract void start();
        protected abstract void end();
    }

    private static class CatonCheckerV1 extends CatonChecker {
        private final HandlerThread mHandlerThread = new HandlerThread("CatonCheckerV1");
        private final Handler mHandler;
        private final Runnable mRun = new Runnable() {
            @Override
            public void run() {
                // 出现卡顿的实时监控点
            }
        };

        private long mCheckStart = 0;

        public CatonCheckerV1(@NonNull CatonSummary summary) {
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
                mSummary.update(System.currentTimeMillis() - mCheckStart);
            }
            mCheckStart = 0;
        }

    }

    private static class CatonCheckerV2 extends CatonChecker {

        private final Handler mMainHandler;
        private CatonCheckerV2Run mRun = null;
        private Thread mThread = null;

        protected CatonCheckerV2(@NonNull CatonSummary summary) {
            super(summary);
            mMainHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        protected void start() {
            if (mThread != null || mRun != null) {
                end();
            }
            if (mThread == null) {
                mRun = new CatonCheckerV2Run(mMainHandler, mSummary);
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

    private static class CatonCheckerV2Run implements Runnable {
        private boolean isInterrupt = false;
        private Handler mMainHandler;
        private CatonSummary mSummary;
        private static final int MSG_UI_EMPTY = 999;
        private int mLastMin = 0;

        private CatonCheckerV2Run(@NonNull Handler handler, @NonNull CatonSummary summary) {
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
                        mSummary.update(mLastMin * 1000);
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
