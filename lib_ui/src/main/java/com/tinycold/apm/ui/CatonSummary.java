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

    public void increaseTimes() {
        mCatonTimes++;
    }

    public void increaseDuration(long d) {
        mCatonDuration += d;
    }

    public int times() {
        return mCatonTimes;
    }

    public long duration() {
        return mCatonDuration;
    }

}
