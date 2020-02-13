package com.demkom58.timings;

import java.util.List;

import static co.aikar.util.JSONUtil.toArray;

/**
 * <p>Lightweight object for tracking timing data</p>
 *
 * This is broken out to reduce memory usage
 */
class TimingData {
    private final int id;
    private int count = 0;
    private int lagCount = 0;
    private long totalTime = 0;
    private long lagTotalTime = 0;
    private int curTickCount = 0;
    private long curTickTotal = 0;

    TimingData(int id) {
        this.id = id;
    }

    private TimingData(TimingData data) {
        this.id = data.id;
        this.totalTime = data.totalTime;
        this.lagTotalTime = data.lagTotalTime;
        this.count = data.count;
        this.lagCount = data.lagCount;
    }

    void add(long diff) {
        ++curTickCount;
        curTickTotal += diff;
    }

    void processTick(boolean violated) {
        totalTime += curTickTotal;
        count += curTickCount;
        if (violated) {
            lagTotalTime += curTickTotal;
            lagCount += curTickCount;
        }
        curTickTotal = 0;
        curTickCount = 0;
    }

    void reset() {
        count = 0;
        lagCount = 0;
        curTickTotal = 0;
        curTickCount = 0;
        totalTime = 0;
        lagTotalTime = 0;
    }

    @Override
    protected TimingData clone() {
        return new TimingData(this);
    }

    List<Object> export() {
        List<Object> list = toArray(
            id,
            count,
            totalTime);
        if (lagCount > 0) {
            list.add(lagCount);
            list.add(lagTotalTime);
        }
        return list;
    }

    boolean hasData() {
        return count > 0;
    }

    long getTotalTime() {
        return totalTime;
    }

    int getCurTickCount() {
        return curTickCount;
    }

    void setCurTickCount(int curTickCount) {
        this.curTickCount = curTickCount;
    }

    long getCurTickTotal() {
        return curTickTotal;
    }

    void setCurTickTotal(long curTickTotal) {
        this.curTickTotal = curTickTotal;
    }
}
