package com.tinycold.apm.ui;

/**
 * 卡顿概况
 */
public class CatonSummary {
    /**
     * 卡顿次数
     */
    private int mCatonTimes = 0;
    /**
     * 卡顿时长
     */
    private long mCatonDuration = 0;

    public void update(long duration) {
        synchronized (this) {
            mCatonTimes++;
            mCatonDuration += duration;
        }
    }

    public int times() {
        synchronized (this) {
            return mCatonTimes;
        }
    }

    public long duration() {
        synchronized (this) {
            return mCatonDuration;
        }
    }

}
